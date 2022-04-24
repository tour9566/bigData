package com.air.antispider.stream.dataprocess.businessprocess

import com.air.antispider.stream.common.util.jedis.{JedisConnectionUtil, PropertiesUtil}
import org.apache.spark.rdd.RDD
import org.json4s.DefaultFormats
import org.json4s.jackson.Json

//实现链路统计功能
object LinkCount {
  //链路统计代码
  def linkCount(rdd: RDD[String]): collection.Map[String, Int] = {

    //每个服务器本批次数据访问了多少次
      //1 遍历rdd获取到每条数据
      val serverCount=rdd.map(message=>{
        //2 抽取出服务器的IP
        var ip=""

        if (message.split("#CS#",-1).length>9){
          ip=message.split("#CS#",-1)(9)
        }

        //3  将ip和1返回
        (ip,1)
      }).reduceByKey(_+_)//4 调用reducebykey  计算出ip和总数



    //当前活跃连接数的计算
      //1获取到一条数据，使用“#CS#”对数据进行切割
       val activeUserCount= rdd.map(message=>{
          var ip =""
          var activeUserCount=""
          //切分数据
          if (message.split("#CS#").length>11){
            //2获取切分后的第十二个数据（角标是11）和第十个数据（角标是9）
            //截取当前活跃连接数
            activeUserCount=message.split("#CS#")(11)
            //截取IP
            ip=message.split("#CS#")(9)
          }
          //3将第十个数据和第十二个数据，进行输出
          (ip,activeUserCount)

        }).reduceByKey((k,v)=>v)//4调用reducebykey（(k,v)=>v）求去每个服务器多个数据中的最后一个数据




      //将计算出来的两个结果写入redis
        //1 在两个数据不为空的前提下，将两个数据转换成两个小的map
          if (!serverCount.isEmpty()  && !activeUserCount.isEmpty()){
            //将两个数据转换成两个小的map
            val serverCountMap=serverCount.collectAsMap()
            val activeUserCountMap=activeUserCount.collectAsMap()

            //2 封装最终要写入redis的数据(将两个小的MAP封装成一个大的MAP)
            //  serversCountMap;  与前端工程师约定好
            //  activeNumMap;  与前端工程师约定好
            val Maps=Map(
              "serversCountMap"->serverCountMap,
              "activeNumMap"->activeUserCountMap
            )
            //3 在配置文件中读取出数据key的前缀，+时间戳（redis中对数据的key）
              val key=PropertiesUtil.getStringByKey("cluster.key.monitor.linkProcess","jedisConfig.properties")+System.currentTimeMillis().toString
            //4 在配置文件中读取出数据的有效存储时间
              val time=PropertiesUtil.getStringByKey("cluster.exptime.monitor","jedisConfig.properties").toInt

            //5 将数据写入redis
            //获取到redis集群
            val redis=JedisConnectionUtil.getJedisCluster
            redis.setex(key,time,Json(DefaultFormats).write(Maps))

          }

    serverCount.collectAsMap()


  }


}
