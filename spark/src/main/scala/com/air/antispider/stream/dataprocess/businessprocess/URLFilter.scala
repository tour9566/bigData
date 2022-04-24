package com.air.antispider.stream.dataprocess.businessprocess

import scala.collection.mutable.ArrayBuffer

//用于数据过滤（过滤无用的数据）
object URLFilter {
  //实现数据的过滤功能
  def filterURL(message: String, FilterRuleList: ArrayBuffer[String]): Boolean = {
    //默认所有的数据都是需要保留的
    var save=true

    //截取出数据中的url
    val request= if(message.split("#CS#").length>1) message.split("#CS#")(1) else ""

    //遍历数据过滤的规则，拿request 与过滤规则进行匹配
    for(rule<-FilterRuleList){
      //若匹配成功表示，需要删除，返回false
      if (request.matches(rule)){
        //将标记改为false
        save=false
      }
    }
    //若匹配不成功表示，表示不需要删除，返回true
    save
  }

}
