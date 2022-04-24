package com.air.antispider.stream.dataprocess.businessprocess

import com.air.antispider.stream.common.bean._
import com.air.antispider.stream.dataprocess.constants.TravelTypeEnum.TravelTypeEnum

//用于实现数据非封装  封装成ProcessedData
object DataPackage {
  //封装ProcessedData
  def dataPackage(str: String, requestMethod: String, requestUrl: String, remoteAddr: String, httpUserAgent: String,
                  timeIso8601: String, serverAddr: String, isFreIP: Boolean, requestType: RequestType,
                  travelType: TravelTypeEnum, cookieValue_JSESSIONID: String, cookieValue_USERID: String,
                  queryRequestData: Option[QueryRequestData], bookRequestData: Option[BookRequestData],
                  httpReferrer: String) = {

    //封装requestParams: CoreRequestParams

    //实例出发地、目的地、起飞时间  的变量
    var depcity=""
    var arrcity =""
    var flightDate=""
    //在查询解析后的数据中抽取出出发地、目的地、起飞时间
    queryRequestData match {
      case Some(datas)=>
        depcity=datas.depCity
        arrcity=datas.arrCity
        flightDate=datas.flightDate
      case None=>
    }

    //若数据在查询的结果中已经封装完了（flightDate有值），则不使用预定的数据封装
    if (flightDate==""){
      //在预定解析后的数据中抽取出出发地、目的地、起飞时间
      bookRequestData  match {
        case Some(datas)=>
          depcity=datas.depCity.toString()
          arrcity=datas.arrCity.toString()
          flightDate=datas.flightDate.toString()
        case None=>
      }
    }

    //封装核心请求参数
    val requestParams: CoreRequestParams=CoreRequestParams(flightDate,depcity,arrcity)

    ProcessedData("", requestMethod, requestUrl,remoteAddr, httpUserAgent, timeIso8601,serverAddr, isFreIP,
      requestType, travelType,requestParams,cookieValue_JSESSIONID, cookieValue_USERID,queryRequestData,
      bookRequestData,httpReferrer)
  }


}
