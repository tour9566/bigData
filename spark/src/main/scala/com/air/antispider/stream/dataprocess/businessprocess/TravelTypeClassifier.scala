package com.air.antispider.stream.dataprocess.businessprocess

import com.air.antispider.stream.dataprocess.constants.TravelTypeEnum
import com.air.antispider.stream.dataprocess.constants.TravelTypeEnum.TravelTypeEnum


//用于实现单程-往返标签
object TravelTypeClassifier {
  //实现打单程往返标签
  def classifyByRefererAndRequestBody(httpReferrer: String): TravelTypeEnum = {
     //https://b2c.csair.com/B2C40/newTrips/static/main/page/booking/index.html?
    // t=R&
    // c1=CAN&
    // c2=PEK&
    // d1=2019-05-16&
    // d2=2019-05-16&
    // at=1&
    // ct=0&
    // it=0

    //实例日期格式的正则
    val regex = "^(\\d{4})-(0\\d{1}|1[0-2])-(0\\d{1}|[12]\\d{1}|3[01])$"
    //实例计数器 实现记录日期格式的数据出现的次数
    var dateCounts=0

    //实例表示最终数据业务了类型变量
    var travelType :TravelTypeEnum=null

    //1使用问号“？”对数据进行切割，获取切割后的第二个数据
    if (httpReferrer.contains("?") && httpReferrer.split("\\?").length>1){

      //2在第一步的基础上，对数据使用“&”进行切割，获取切割后的所有数据
      val params=httpReferrer.split("\\?")(1).split("&")
      //3遍历每一个数据，在使用“=” 进行切割，获取切割后的第二个数据
      for(param<-params){
        val keyAndValue=param.split("=")
        //4用第三步的数据匹配日期的正则
        if (keyAndValue.length>1 && keyAndValue(1).matches(regex)){
          //5匹配成功计数器加一（计数器用于记录日期格式的数据出现的次数）
          dateCounts+=1
        }
      }
    }
    //6根据计数器的值返回数据（OneWay，RoundTrip，Unknown）
      if(dateCounts==0){//一个日期格式的数据都没有   其他
        travelType=TravelTypeEnum.Unknown
      }else if (dateCounts==1){//出现1个日期格式的数据   单程
        travelType=TravelTypeEnum.OneWay
      }else if (dateCounts==2){//出现2个日期格式的数据   往返
        travelType=TravelTypeEnum.RoundTrip
      }
    travelType
  }

}
