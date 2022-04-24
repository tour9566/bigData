package com.air.antispider.stream.dataprocess.launch

import com.air.antispider.stream.common.bean.{BookRequestData, CoreRequestParams, QueryRequestData, RequestType}
import com.air.antispider.stream.common.util.jedis.{JedisConnectionUtil, PropertiesUtil}
import com.air.antispider.stream.common.util.log4j.LoggerLevels
import com.air.antispider.stream.dataprocess.businessprocess._
import com.air.antispider.stream.dataprocess.constants.TravelTypeEnum.TravelTypeEnum
import kafka.serializer.StringDecoder
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable.ArrayBuffer

//数据预处理程序的主程序
object DataProcessLauncher {


  //程序主入口
  def main(args: Array[String]): Unit = {
    //添加日志级别的设置
    LoggerLevels.setStreamingLogLevels()
    //当应用被停止的时候，进行如下设置可以保证当前批次执行完之后再停止应用。
    System.setProperty("spark.streaming.stopGracefullyOnShutdown", "true")

    //1、	创建Spark conf
    val conf=new SparkConf().setAppName("DataProcess").setMaster("local[2]")
      .set("spark.metrics.conf.executor.source.jvm.class", "org.apache.spark.metrics.source.JvmSource")//开启集群监控功能

    //2、	创建SparkContext
    val sc=new SparkContext(conf)

    //      kafkaParams: JMap[String, String],
    //      topics: JSet[String]

    val kafkaParams=Map("bootstrap.servers"->PropertiesUtil.getStringByKey("default.brokers","kafkaConfig.properties"))
    val topics=Set(PropertiesUtil.getStringByKey("source.nginx.topic","kafkaConfig.properties"))


    //数据预处理的程序
    val ssc=setupSsc(sc,kafkaParams,topics)

    //6、	开启Streaming任务+开启循环
    ssc.start()
    ssc.awaitTermination()

  }

  //数据预处理的程序
  def setupSsc(sc: SparkContext, kafkaParams: Map[String, String], topics: Set[String]): StreamingContext = {
    //程序初始化阶段
    //实例redis  为后续使用
    val redis =JedisConnectionUtil.getJedisCluster

    //3 、	创建Streaming Context
    val ssc=new StreamingContext(sc,Seconds(2))

    //读取数据库中的数据过滤规则
    var filterRuleList: ArrayBuffer[String]=AnalyzeRuleDB.queryFilterRule()
    //将过滤规则添加到广播变量
    @volatile  var broadcastFilterRuleList= sc.broadcast(filterRuleList)

    //读取数据分类规则（四种规则，每种单独读取）到预处理程序
    var RuleMaps=AnalyzeRuleDB.queryRuleMap()
    @volatile  var broadcastRuleMaps=sc.broadcast(RuleMaps)

    ////1读取数据库内的数据解析规则到预处理程序（将表内的所有查询规则数据全部读取到程序内）
    //数据解析规则-- 查询类
    var  queryRule=AnalyzeRuleDB.queryRule(0)
    //2将规则加载到广播变量
    @volatile  var broadcastQueryRules=sc.broadcast(queryRule)
    //数据解析规则-- 预定类
    var  bookRule=AnalyzeRuleDB.queryRule(1)
    @volatile  var broadcastBookRules=sc.broadcast(bookRule)

    //1读取数据库内历史出现过的黑名单数据到预处理程序中
    var blackIpList=AnalyzeRuleDB.getBiackIpDB()
    //2将历史爬虫添加到广播变量、并循环判断是否需要更新（内含多个步骤，此处省略）
    @volatile var  broadcastBlackIpList= sc.broadcast(blackIpList)


    //4 、	读取kafka内的数据ssc,kafkaParams,topics)
    val kafkaData=KafkaUtils.createDirectStream[String,String,StringDecoder,StringDecoder](ssc,kafkaParams,topics)
    //真正的数据
    val kafkaValue=kafkaData.map(_._2)



    //5 、	消费数据
    kafkaValue.foreachRDD(rdd=>{  //迭代运行（每2秒运行一次）

      //到redis读取是否需要更新的标记
      val NeedUpDateFilterRule=redis.get("NeedUpDateFilterRule")
      //判断是否需要更新    若数据不为空并且数据转成Boolean为true  表示需要更新
      if(!NeedUpDateFilterRule.isEmpty  && NeedUpDateFilterRule.toBoolean){
        //若需要更新，那么在数据库中重新读取新的过滤规则到程序中
        filterRuleList=AnalyzeRuleDB.queryFilterRule()
        //将广播变量清空
        broadcastFilterRuleList.unpersist()
        //将新的规则重新加载到广播变量
        broadcastFilterRuleList= sc.broadcast(filterRuleList)
        //将redis内是否需要更新规则的标识改为“false”
        redis.set("NeedUpDateFilterRule","false")
      }

      //到redis读取是否需要更新的标记
      val NeedUpDateClassifyRule= redis.get("NeedUpDateClassifyRule")
      //判断是否需要更新  若数据不为空并且数据转成Boolean为true  表示需要更新
      if(!NeedUpDateClassifyRule.isEmpty && NeedUpDateClassifyRule.toBoolean){
        //若需要更新，那么在数据库中重新读取新的四种业务规则到程序中
        RuleMaps=AnalyzeRuleDB.queryRuleMap()
        //将广播变量清空
        broadcastRuleMaps.unpersist()
        //将新的分类规则重新加载到广播变量
        broadcastRuleMaps=sc.broadcast(RuleMaps)
        //将redis内是否需要更新规则的标识改为“false”
        redis.set("NeedUpDateClassifyRule","false")
      }



      //环判断是否需要更新（内含多个步骤，此处省略）
      val needUpDataAnalyzeRule=redis.get("NeedUpDataAnalyzeRule")
      //如果获取的数据是非空的，并且这个值是true,那么就进行数据的更新操作（在数据库中重新读取数据加载到Redis）
      if(!needUpDataAnalyzeRule.isEmpty&& needUpDataAnalyzeRule.toBoolean){
        //重新读取mysql的数据
        queryRule=AnalyzeRuleDB.queryRule(0)
        bookRule=AnalyzeRuleDB.queryRule(1)
        //清空广播变量中的数据
        broadcastQueryRules.unpersist()
        broadcastBookRules.unpersist()

        //重新载入新的过滤数据
        broadcastQueryRules=sc.broadcast(queryRule)
        broadcastBookRules=sc.broadcast(bookRule)
        //更新完毕后，将Redis中的true 改成false
        redis.set("NeedUpDataAnalyzeRule","false")
      }

      //判断是否需要更新历史黑名单数据
     val NeedUpDateBlackIPList= redis.get("NeedUpDateBlackIPList")
      if(!NeedUpDateBlackIPList.isEmpty && NeedUpDateBlackIPList.toBoolean){
        blackIpList=AnalyzeRuleDB.getBiackIpDB()
        broadcastBlackIpList.unpersist()
        broadcastBlackIpList= sc.broadcast(blackIpList)
        redis.set("NeedUpDateBlackIPList","false")
      }






      //1 链路统计功能
      //将每个服务器本批次数据访问了多少次返回
     val serverCount= LinkCount.linkCount(rdd)

      //2 数据清洗功能
      //定义方法，参数为一条数据和广播变量
      val filteredData=rdd.filter(message=>URLFilter.filterURL(message,broadcastFilterRuleList.value))
      //filteredData.foreach(println)

      //数据预处理
      //数据脱敏建立在数据清洗之后
      val DataProcess= filteredData.map(message=>{
        //3 数据脱敏功能
        //3-1 手机号码脱敏
        val encryptedPhone= EncryptedData.encryptedPhone(message)

        //3-2 身份证号码脱敏
        val encryptedId= EncryptedData.encryptedId(encryptedPhone)
        //encryptedId

        //4 数据拆分功能（一劳永逸）
        val (requestUrl,requestMethod,contentType,requestBody,httpReferrer,remoteAddr,httpUserAgent,timeIso8601,
        serverAddr,cookiesStr,cookieValue_JSESSIONID,cookieValue_USERID) =DataSplit.dataSplit(encryptedId)

        //requestMethod

        //5 数据分类功能（打标签）
        //5-1  飞行类型与操作类型
        //操作类型: 0查询 1预订   航班类型: 0国内 1国际
        //0                              0   国内查询
        //0                              1   国际查询
        //1                              0   国内预定
        //1                              1   国际预定
        //定义方法，参数为经过拆分后的URL 和分类的广播变量
        val requestType: RequestType= RequestTypeClassifier.classifyByRequest(requestUrl,broadcastRuleMaps.value)
        //requestType

        //5-2   单程/往返
        val travelType: TravelTypeEnum=TravelTypeClassifier.classifyByRefererAndRequestBody(httpReferrer)
        //travelType


        //6 数据的解析
        //6-1 查询类数据的解析
        //1前面
        //2前面
        //3 对数据进行解析（在多种解析规则的情况下，确定最终使用哪一个规则进行解析）
        val queryRequestData = AnalyzeRequest.analyzeQueryRequest( requestType, requestMethod, contentType,
                              requestUrl, requestBody, travelType, broadcastQueryRules.value)
//        val data= queryRequestData match {
//          case Some(datas)=> datas.flightDate
//        }
//        data
        //6-2 预定类数据的解析

        val bookRequestData = AnalyzeBookRequest.analyzeBookRequest( requestType, requestMethod,
                                contentType, requestUrl, requestBody, travelType, broadcastBookRules.value)



        //7 历史爬虫判断
        //1
        //2
        //3将数据中的ip与历史出现过的黑名单IP数据进行对比，判断是否相等
        val isFreIP= IpOperation.isFreIP(remoteAddr,broadcastBlackIpList.value)





        //8 数据结构化
        val  processedData=DataPackage.dataPackage("", requestMethod, requestUrl,remoteAddr, httpUserAgent, timeIso8601,serverAddr, isFreIP,
          requestType, travelType,cookieValue_JSESSIONID, cookieValue_USERID,queryRequestData, bookRequestData,httpReferrer)

        processedData

      })

      DataProcess.foreach(println)

      //9 数据推送
      //9-1 查询类数据的推送
      DataSend.sendQueryDataToKafka(DataProcess)
      //9-2 预定类数据的推送
      DataSend.sendBookDataToKafka(DataProcess)


      //10 系统监控功能（数据预处理的监控功能）
      SparkStreamingMonitor.streamMonitor(sc,rdd,serverCount,redis)





    })



    ssc
  }






}
