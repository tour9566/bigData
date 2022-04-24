package com.air.antispider.stream.rulecompute.launch

import java.text.SimpleDateFormat

import com.air.antispider.stream.common.util.hdfs.{BlackListToHDFS, BlackListToRedis}
import com.air.antispider.stream.common.util.jedis.{JedisConnectionUtil, PropertiesUtil}
import com.air.antispider.stream.common.util.log4j.LoggerLevels
import com.air.antispider.stream.dataprocess.businessprocess.{AnalyzeRuleDB, SparkStreamingMonitor}
import com.air.antispider.stream.rulecompute.businessprocess._
import kafka.serializer.StringDecoder
import org.apache.spark.sql.{Row, SQLContext}
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable.ArrayBuffer


//用于实现爬虫识别的功能
//爬虫识别的主程序
object RuleComputeLauncher {


  //爬虫识别程序的主入口
  def main(args: Array[String]): Unit = {
    //添加日志级别的设置
    LoggerLevels.setStreamingLogLevels()
    //当应用被停止的时候，进行如下设置可以保证当前批次执行完之后再停止应用。
    System.setProperty("spark.streaming.stopGracefullyOnShutdown", "true")

    //1、	创建Spark conf
    val conf=new SparkConf().setAppName("RuleCompute").setMaster("local[2]")
      .set("spark.metrics.conf.executor.source.jvm.class", "org.apache.spark.metrics.source.JvmSource")//开启集群监控功能

    //2、	创建SparkContext
    val sc=new SparkContext(conf)

    //      kafkaParams: JMap[String, String],
    //      topics: JSet[String]

    val kafkaParams=Map("bootstrap.servers"->PropertiesUtil.getStringByKey("default.brokers","kafkaConfig.properties"))
    val topics=Set(PropertiesUtil.getStringByKey("source.query.topic","kafkaConfig.properties"))


    //数据预处理的程序
    val ssc=setupRuleComputeSsc(sc,kafkaParams,topics)

    //6、	开启Streaming任务+开启循环
    ssc.start()
    ssc.awaitTermination()


  }



  //爬虫识别代码
  def setupRuleComputeSsc(sc: SparkContext, kafkaParams: Map[String, String], topics: Set[String]) = {

    //实例SQL context
   val sqlContext= new SQLContext(sc)

    //3 、	创建Streaming Context
    val ssc=new StreamingContext(sc,Seconds(2))

    //实例redis  为后续使用
    val jedis =JedisConnectionUtil.getJedisCluster

    //读取关键页面到程序
    var criticalPagesList=AnalyzeRuleDB.queryCriticalPages()
    //将数据添加到广播变量
    @volatile var broadcastCriticalPagesList=  sc.broadcast(criticalPagesList)
    //获取流程规则策略配置
    var flowList = AnalyzeRuleDB.createFlow(0)
    @volatile var broadcastFlowList = sc.broadcast(flowList)

    //4 读取kafka 数据
    val kafkaDatas= KafkaUtils.createDirectStream[String,String,StringDecoder,StringDecoder](ssc,kafkaParams,topics)
    //获取value数据
    val kafkaValues= kafkaDatas.map(_._2)

    //判断是否需要更新
    kafkaValues.foreachRDD(rdd=>{
      //关键页面是否改变标识
      val needUpdateQueryCriticalPages = jedis.get("NeedUpDateQueryCriticalPages")
      if (!needUpdateQueryCriticalPages.isEmpty() && needUpdateQueryCriticalPages.toBoolean) {
        criticalPagesList = AnalyzeRuleDB.queryCriticalPages()
        broadcastCriticalPagesList.unpersist()
        broadcastCriticalPagesList = sc.broadcast(criticalPagesList)
        jedis.set("NeedUpDateQueryCriticalPages", "false")
      }

      //流程规则策略变更标识
      val needUpDateflowList = jedis.get("NeedUpDateflowList")
      //Mysql-流程规则策略是否改变标识
      if (!needUpDateflowList.isEmpty() && needUpDateflowList.toBoolean) {
        flowList = AnalyzeRuleDB.createFlow(0)
        broadcastFlowList.unpersist()
        broadcastFlowList = sc.broadcast(flowList)
        jedis.set("NeedUpDateflowList", "false")
      }
    })

    //5 消费数据
    // kafkaValues.foreachRDD(rdd=>rdd.foreach(println))
    //数据加载封装
    //将kafka内的数据使用“#CS#”拆分，拆分后封装成processedData
    val processedData = QueryDataPackage.queryDataLoadAndPackage(kafkaValues)
    //processedData.foreachRDD(rdd=>rdd.foreach(println))

    //计算爬虫识别所需要的八个指标
    //1 按 IP 段聚合  -  5  分钟内的  IP  段（IP  前两位）访问量
        val ipBlockCounts= CoreRule. ipBlockCounts(processedData)
        //将最终数据转换为Map,便于后续提取使用。
        var ipBlockCountsMap  : collection.Map[String, Int] =null
        ipBlockCounts.foreachRDD(rdd=>{
          ipBlockCountsMap=rdd.collectAsMap()
        })

        // ipBlockCounts.foreachRDD(rdd=>rdd.foreach(println))


    //2 按 IP 地址聚合  -  某个  IP，5  分钟内总访问量
    val ipCounts= CoreRule.ipCounts(processedData)
    //将最终数据转换为Map,便于后续提取、使用。
    var ipCountsMap: collection.Map[String, Int] =null
    ipCounts.foreachRDD(rdd=>{
      ipCountsMap=rdd.collectAsMap()
    })
    //ipCounts.foreachRDD(rdd=>rdd.foreach(println))


    //3 按 IP 地址聚合  -  某个  IP，5  分钟内的关键页面访问总量
     val criticalPagesCounts=CoreRule.criticalPagesCounts(processedData,broadcastCriticalPagesList.value)
    //将最终数据转换为Map,便于后续提取、使用。
    var criticalPagesCountsMap: collection.Map[String, Int]=null
    criticalPagesCounts.foreachRDD(rdd=>{
      criticalPagesCountsMap=rdd.collectAsMap()
    })

    //criticalPagesCounts.foreachRDD(rdd=>rdd.foreach(println))


    //4 按 IP 地址聚合  -  某个  IP，5  分钟内的  UA  种类数统计
   val userAgentCounts= CoreRule.userAgent(processedData)
    //将最终数据转换为Map,便于后续提取、使用。
    var userAgentCountsMap: collection.Map[String, Int]=null
    userAgentCounts.foreachRDD(rdd=>{
      userAgentCountsMap=rdd.collectAsMap()
    })

    //userAgentCounts.foreachRDD(rdd=>rdd.foreach(println))



    //5 按 IP 地址聚合  -  某个  IP，5  分钟内查询不同行程的次数
    val differentJourneysCounts=CoreRule.differentJourneys(processedData)
        //将最终数据转换为Map,便于后续提取、使用。
    var differentJourneysCountsMap: collection.Map[String, Int]=null
    differentJourneysCounts.foreachRDD(rdd=>{
      differentJourneysCountsMap=rdd.collectAsMap()
    })

   // differentJourneysCounts.foreachRDD(rdd=>rdd.foreach(println))

    //6 按 IP 地址聚合  -  某个  IP，5  分钟内访问关键页面的 Cookie 数
    val ipCookCount=CoreRule.criticalCookies(processedData,broadcastCriticalPagesList.value)
    //将最终数据转换为Map,便于后续提取、使用。
    var ipCookCountMap: collection.Map[String, Int]=null
    ipCookCount.foreachRDD(rdd=>{
      ipCookCountMap=rdd.collectAsMap()
    })

   // ipCookCount.foreachRDD(rdd=>rdd.foreach(println))


    // 7 按 IP 地址聚合  -  某个  IP，5  分钟内的关键页面最短访问间隔
     val minTimeDiff =CoreRule.criticalPagesMinTime(processedData,broadcastCriticalPagesList.value)
      //14  将最终数据转换为Map,便于后续提取、使用。
     var minTimeDiffMap: collection.Map[String, Int]=null
    minTimeDiff.foreachRDD(rdd=>{
      minTimeDiffMap= rdd.collectAsMap()
    })

   // minTimeDiff.foreachRDD(rdd=>rdd.foreach(println))


    // 8按 IP 地址聚合  -  某个  IP，  5  分钟内小于最短访问间隔（自设）的关键页面查询次数
     val lessDefaultTimes=CoreRule.lessDefaultTimes(processedData,broadcastCriticalPagesList.value,broadcastFlowList.value)
    //14 将最终数据转换为Map,便于后续提取、使用。
    var lessDefaultTimesMap: collection.Map[String, Int]=null
    lessDefaultTimes.foreachRDD(rdd=>{
      lessDefaultTimesMap= rdd.collectAsMap()
    })
   // lessDefaultTimes.foreachRDD(rdd=>rdd.foreach(println))

    //爬虫识别
        //1  指标碰撞
        //2  最终的打分
        //3  爬虫判断（结论：是/不是）
    val antiCalculateResults=processedData.map(message=>{
      //获取数据中ip
      val ip =message.remoteAddr
      //返回结果是最终的返回结果  AntiCalculateResult
      val antiCalculateResult=RuleUtil.calculateAntiResult(message,ip,ipBlockCountsMap,ipCountsMap,criticalPagesCountsMap,userAgentCountsMap,
        differentJourneysCountsMap,ipCookCountMap,minTimeDiffMap,lessDefaultTimesMap,broadcastFlowList.value)

      antiCalculateResult
    })


    //数据入库
        //1   过滤非爬虫数据（只保留爬虫数据）
            //1.1、	打分后的数据的rdd调用filter对数据进行过滤
            val allBlackDatas=  antiCalculateResults.filter(message=>{
                //1.2、添加此数据是否是爬虫的标记
                var isspider=false
                //1.3、	遍历经过打分后的数据（已经得出结论：这个数据是爬虫/不是爬虫）
                //1.4、	遍历每一个经过计算的数据，获取出每个数据的flowsScore，遍历flowsScore内的isUpLimited（true/false）
               val flowsScores= message.flowsScore
                for(flowsScore<-flowsScores){
                  //1.5、	若数据内的isUpLimited是true 那么表述这个数据是爬虫，将标记改为true
                  if (flowsScore.isUpLimited){
                    isspider=true
                  }
                  //1.6、	若数据内的isUpLimited是false那么表述这个数据不是爬虫，标记不做任何改动（默认标记就是false）

                }
                isspider
              })

             //allBlackDatas.foreachRDD(rdd=>rdd.foreach(println))




        //2   对爬虫数据进行去重操作
          //2.1、	遍历每一个黑名单数据，将数据的ip作为key, flowsScore作为value获取出来
           val blackDatas= allBlackDatas.map(message=>{
              //获取数据的IP
              val ip=message.ip
              //获取数据的flowsScore
              val flowsScore=message.flowsScore
              //2.2、	将两个数据返回
              (ip,flowsScore)

            }).reduceByKey((k,v)=>v) //2.3、	调用reducebykey((k,v)=>v) 取出相同的数据中的最后一个

            blackDatas.foreachRDD(rdd=>rdd.foreach(println))


        //3   数据入库（Redis）
            //1,遍历数据中的RDD，将其序列化（collect（））（ 不序列化汇报错：Task not serializable）
            blackDatas.foreachRDD(rdd=>{
             val blackDatas: Array[(String, Array[FlowScoreResult])] = rdd.collect()
              //2、	遍历序列后的每一个数据，获取出数据中的ip和flowsScore
              for(blackData<-blackDatas){


                // 黑名单Redis数据恢复
                BlackListToRedis.blackListDataToRedis(jedis, sc, sqlContext)


                //黑名单DataFrame-备份到HDFS
                //写入Redis前，准备存储数据的Array
                val antiBlackListDFR = new ArrayBuffer[Row]()



                //获取数据中的IP
                val ip=blackData._1
                //获取数据中的flowsScore
                val flowsScores: Array[FlowScoreResult]=blackData._2
                //3、	遍历flowsScore，为写入Redis准备数据
                for(flowsScore<-flowsScores){
                  //4、	准备写入redis 的key,在配置文件中读取出key的前缀 +数据的IP +流程id+时间戳
                  val key=PropertiesUtil.getStringByKey("cluster.key.anti_black_list","jedisConfig.properties")+
                    "|"+ip+"|"+flowsScore.flowId+"|"+System.currentTimeMillis().toString
                  //5、	准备写入redis 的time,在配置文件中读取出数据的保留时间
                  val time =PropertiesUtil.getStringByKey("cluster.exptime.anti_black_list","jedisConfig.properties").toInt
                  //6、	准备写入redis 的value,value包括计算的最终分数+命中的规则+命中的时间
                  val value=flowsScore.flowScore+"|"+flowsScore.hitRules+"|"+flowsScore.hitTime
                  //7、	将数据写入redis
                  jedis.setex(key,time,value)


                  //添加黑名单DataFrame-备份到ArrayBuffer
                  //写入redis后立刻将数据添加到antiBlackListDFR
                  antiBlackListDFR.append(Row(((PropertiesUtil.getStringByKey("cluster.exptime.anti_black_list",
                    "jedisConfig.properties").toLong) * 1000 + System.currentTimeMillis()).toString,
                    key, value))
                }

                //增加黑名单数据实时存储到HDFS的功能-黑名单数据持久化-用于Redis数据恢复
                BlackListToHDFS.saveAntiBlackList(sc.parallelize(antiBlackListDFR), sqlContext)





              }
            })



            // 黑名单数据实时存储hdfs，用于Redis数据恢复

            // 黑名单Redis数据恢复

            // 存储规则计算结果RDD到HDFS
            kafkaValues.foreachRDD(message =>{
              val date = new SimpleDateFormat("yyyy/MM/dd/HH").format(System.currentTimeMillis())
              val yyyyMMddHH = date.replace("/","").toInt
              val path: String = PropertiesUtil.getStringByKey("blackListPath","HDFSPathConfig.properties")+"itcast/"+yyyyMMddHH
              try{
                sc.textFile(path+"/part-00000").union(message).repartition(1).saveAsTextFile(path)
              }catch{
                case e: Exception =>
                  message.repartition(1).saveAsTextFile(path)
              }
              //设置任务监控
               SparkStreamingMonitor.queryMonitor(sc, message)
            })










    ssc
  }





}
