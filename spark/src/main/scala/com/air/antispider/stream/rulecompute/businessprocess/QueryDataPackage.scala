package com.air.antispider.stream.rulecompute.businessprocess

import com.air.antispider.stream.common.bean._
import com.air.antispider.stream.dataprocess.constants.TravelTypeEnum.TravelTypeEnum
import com.air.antispider.stream.dataprocess.constants.{BehaviorTypeEnum, FlightTypeEnum, TravelTypeEnum}
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import org.apache.spark.streaming.dstream.DStream

//将kafka内的数据使用“#CS#”拆分，拆分后封装成processedData
object QueryDataPackage {
  //分割封装
  def queryDataLoadAndPackage(kafkaValues: DStream[String]) = {
    //使用mapPartitions减少包装类的创建开销
    kafkaValues.mapPartitions { partitionsIterator =>
      //创建json解析
      val mapper = new ObjectMapper
      mapper.registerModule(DefaultScalaModule)
      //将数据进行map，一条条处理
      partitionsIterator.map { sourceLine =>
        //分割数据
        val dataArray = sourceLine.split("#CS#", -1)
        //原始数据，站位，并无数据
        val sourceData = dataArray(0)
        val requestMethod = dataArray(1)
        val request = dataArray(2)
        val remoteAddr = dataArray(3)
        val httpUserAgent = dataArray(4)
        val timeIso8601 = dataArray(5)
        val serverAddr = dataArray(6)
        val highFrqIPGroup: Boolean = dataArray(7).equalsIgnoreCase("true")
        val requestType: RequestType = RequestType(FlightTypeEnum.withName(dataArray(8)), BehaviorTypeEnum.withName(dataArray(9)))
        val travelType: TravelTypeEnum = TravelTypeEnum.withName(dataArray(10))
        val requestParams: CoreRequestParams = CoreRequestParams(dataArray(11), dataArray(12), dataArray(13))
        val cookieValue_JSESSIONID: String = dataArray(14)
        val cookieValue_USERID: String = dataArray(15)
        //分析查询请求的时候不需要book数据
        val bookRequestData: Option[BookRequestData] = None
        //封装query数据
        val queryRequestData = if (!dataArray(16).equalsIgnoreCase("NULL")) {
          mapper.readValue(dataArray(16), classOf[QueryRequestData]) match {
            case value if value != null => Some(value)
            case _ => None
          }
        } else {
          None
        }
        val httpReferrer = dataArray(18)
        //封装流程数据，返回
        ProcessedData("", requestMethod, request,
          remoteAddr, httpUserAgent, timeIso8601,
          serverAddr,  highFrqIPGroup,
          requestType, travelType, requestParams,
          cookieValue_JSESSIONID, cookieValue_USERID,
          queryRequestData, bookRequestData, httpReferrer)
      }
    }
  }


}
