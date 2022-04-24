package com.air.antispider.stream.dataprocess.businessprocess

import java.text.SimpleDateFormat
import java.util.Date

import com.air.antispider.stream.common.util.jedis.{JedisConnectionUtil, PropertiesUtil}
import com.air.antispider.stream.common.util.spark.SparkMetricsUtils
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.json4s.DefaultFormats
import org.json4s.jackson.Json
import redis.clients.jedis.JedisCluster

//用于实现系统监控的过程
object SparkStreamingMonitor {

  //实现系统监控功能
  def streamMonitor(sc: SparkContext, rdd: RDD[String], serverCount: collection.Map[String, Int], redis: JedisCluster) = {

    //1计算总的数据量
    val dataCount = rdd.count()
    //2通过	http://localhost:4040/metrics/json/  url获取json数据
    val url = "http://localhost:4040/metrics/json/"
    val jsonData = SparkMetricsUtils.getMetricsJson(url)

    //3获取“gauges”对应的数据
    val gaugesJson = jsonData.getJSONObject("gauges")

    //获取任务的ID  任务的名称
    val appId = sc.applicationId
    val appName = sc.appName
    //4拼接结束时间的path
    val endTimePath = appId + ".driver." + appName + ".StreamingMetrics.streaming.lastCompletedBatch_processingEndTime"
    //5拼接开始时间的path
    val startTimePath = appId + ".driver." + appName + ".StreamingMetrics.streaming.lastCompletedBatch_processingStartTime"

    //6获取出开始时间和结束时间
    //获取出开始时间
    val tmpStartTime = gaugesJson.getJSONObject(startTimePath)
    //后去最终的开始时间戳
    var startTime: Long = 0
    if (tmpStartTime != null) {
      startTime = tmpStartTime.getLong("value")
    }

    //获取出结束时间
    val tmpEndTime = gaugesJson.getJSONObject(endTimePath)
    //后去最终的开始时间戳
    var endTime: Long = 0
    if (tmpEndTime != null) {
      endTime = tmpEndTime.getLong("value")
    }

    //7求出时间差（结束时间-开始时间）
    val runTime = endTime - startTime

    //将时间戳格式的结束时间转换成年月日格式的数据
    val simple = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val endTimeS = simple.format(new Date(endTime))

    if (runTime > 0) {
      //8计算任务运行的速度（数据总量/时间）
      val runSpeed = dataCount.toFloat / runTime.toFloat

      //9封装展现需要的数据
      //将必要的数据转换成Map
      val Maps = Map(
        "costTime" -> runTime.toString,
        "serversCountMap" -> serverCount,
        "applicationId" -> appId.toString,
        "countPerMillis" -> runSpeed.toString,
        "applicationUniqueName" -> appName.toString,
        "endTime" -> endTimeS,
        "sourceCount" -> dataCount.toString
      )
      //与前端工程师约定一个规则
      //    private String costTime;//时间差  任务运行时间（end-start）
      //    private Map<String,Object> serversCountMap; //链路统计中的结果
      //    private String applicationId;//appID
      //    private String countPerMillis; //speed
      //    private String applicationUniqueName; //appname
      //    private String endTime; //任务运行结尾时间
      //    private String sourceCount;  //一批次数据数据总量


      //10将数据写入redis
      //准备写入redis需要的key
      val key = PropertiesUtil.getStringByKey("cluster.key.monitor.dataProcess", "jedisConfig.properties") + System.currentTimeMillis().toString
      //准备写入redis需要的时间（数据的有效存储时间，超过这个时间数据在redis内自动删除）
      val time = PropertiesUtil.getStringByKey("cluster.exptime.monitor", "jedisConfig.properties").toInt

      redis.setex(key, time, Json(DefaultFormats).write(Maps))

    }

  }




//爬虫识别的效率监控
  def queryMonitor(sc: SparkContext, message: RDD[String]): Unit = {
    //done:Spark性能实时监控
    //监控数据获取
    val sourceCount = message.count()
    // val sparkDriverHost = sc.getConf.get("spark.org.apache.hadoop.yarn.server.webproxy.amfilter.AmIpFilter.param.PROXY_URI_BASES")
    //监控信息页面路径为集群路径+/proxy/+应用id+/metrics/json
    //val url = s"${sparkDriverHost}/metrics/json"
    //local模式的路径
    val url = "http://localhost:4041/metrics/json/"
    val jsonObj = SparkMetricsUtils.getMetricsJson(url)
    //应用的一些监控指标在节点gauges下
    val result = jsonObj.getJSONObject("gauges")
    //监控信息的json路径：应用id+.driver.+应用名称+具体的监控指标名称
    //最近完成批次的处理开始时间-Unix时间戳（Unix timestamp）-单位：毫秒
    val startTimePath = sc.applicationId + ".driver." + sc.appName + ".StreamingMetrics.streaming.lastCompletedBatch_processingStartTime"
    val startValue = result.getJSONObject(startTimePath)
    var processingStartTime: Long = 0
    if (startValue != null) {
      processingStartTime = startValue.getLong("value")
    }
    //最近完成批次的处理结束时间-Unix时间戳（Unix timestamp）-单位：毫秒
    val endTimePath = sc.applicationId + ".driver." + sc.appName + ".StreamingMetrics.streaming.lastCompletedBatch_processingEndTime"
    val endValue = result.getJSONObject(endTimePath)
    var processingEndTime: Long = 0
    if (endValue != null) {
      processingEndTime = endValue.getLong("value")
    }
    //流程所用时间
    val processTime = processingEndTime - processingStartTime

    //监控数据推送
    //done:实时处理的速度监控指标-monitorIndex需要写入Redis，由web端读取Redis并持久化到Mysql
    val endTime = processingEndTime
    val costTime = processTime
    val countPerMillis = sourceCount.toFloat / costTime.toFloat
    val monitorIndex = (endTime, sc.appName, sc.applicationId, sourceCount, costTime, countPerMillis)

    val format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val processingEndTimeString = format.format(new Date(processingEndTime))

    val fieldMap =  Map(
      "endTime" -> processingEndTimeString,
      "applicationUniqueName" -> monitorIndex._2.toString,
      "applicationId" -> monitorIndex._3.toString,
      "sourceCount" -> monitorIndex._4.toString,
      "costTime" -> monitorIndex._5.toString,
      "countPerMillis" -> monitorIndex._6.toString,
      "serversCountMap" -> Map[String, Int]())
    //将数据存入Redis中
    try {
      val jedis = JedisConnectionUtil.getJedisCluster
      //
      val keyName = PropertiesUtil.getStringByKey("cluster.key.monitor.query", "jedisConfig.properties") + System.currentTimeMillis.toString
      //保存监控数据
      jedis.setex(keyName, PropertiesUtil.getStringByKey("cluster.exptime.monitor", "jedisConfig.properties").toInt, Json(DefaultFormats).write(fieldMap))

      //JedisConnectionUtil.returnRes(jedis)
    } catch {
      case e: Exception =>
        e.printStackTrace()
    }
  }




}
