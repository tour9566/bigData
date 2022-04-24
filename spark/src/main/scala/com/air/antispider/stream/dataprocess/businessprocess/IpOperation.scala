package com.air.antispider.stream.dataprocess.businessprocess

import scala.collection.mutable.ArrayBuffer

//用于实现数据是否在历史黑名单出现过
object IpOperation {
  //判断数据是否在历史黑名单出现过
  def isFreIP(remoteAddr: String, blackIpList: ArrayBuffer[String]) = {
    //4若有任意一个是相等的返回true  ,反之返回false
    //实例变量  表示数据是否在历史黑名单出现过
    var isFreIP=false
    //遍历每一个历史出现过的IP
    for(blackIp<-blackIpList){
      //表示出现过
      if (remoteAddr.equals(blackIp)){
        isFreIP=true
      }
    }

    isFreIP

  }

}
