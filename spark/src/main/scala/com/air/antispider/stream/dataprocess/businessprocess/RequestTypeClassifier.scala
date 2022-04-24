package com.air.antispider.stream.dataprocess.businessprocess

import java.util

import com.air.antispider.stream.common.bean.RequestType
import com.air.antispider.stream.dataprocess.constants.BehaviorTypeEnum.BehaviorTypeEnum
import com.air.antispider.stream.dataprocess.constants.{BehaviorTypeEnum, FlightTypeEnum}
import com.air.antispider.stream.dataprocess.constants.FlightTypeEnum.FlightTypeEnum

import scala.collection.mutable.ArrayBuffer

//用于实现数据分类的代码编写
object RequestTypeClassifier {
  //实现航线类型操作类型的标签
  def classifyByRequest(requestUrl: String, RuleMaps: util.HashMap[String, ArrayBuffer[String]]) = {

    //1先在分类的广播变量中获取四种业务场景的规则
    //国内查询规则
    val nqRuleList=RuleMaps.get("nqRuleList")

    //国际查询规则
    val iqRuleList=RuleMaps.get("iqRuleList")

    //国内预定规则
    val nbRuleList=RuleMaps.get("nbRuleList")

    //国际预定规则
    val ibRuleList=RuleMaps.get("ibRuleList")

    //定义返回的类型
    var requestType:RequestType=null

    //守护进程   true表示继续执行
    var flag=true


    //2遍历四种规则与数据进行匹配
    //遍历国内查询规则
    for(nqRule<-nqRuleList  if flag){
      //使用数据匹配国内查询的规则   若匹配成功表示  这个url 所在的数据就是“国内查询”
      if (requestUrl.matches(nqRule)){
        flag=false
        //到了这一步就可以断定这个数据的业务类型
        requestType=RequestType(FlightTypeEnum.National,BehaviorTypeEnum.Query)
      }
    }

    //遍历国际查询规则
    for(iqRule<-iqRuleList  if flag){
      if (requestUrl.matches(iqRule)){
        flag=false
        requestType=RequestType(FlightTypeEnum.International,BehaviorTypeEnum.Query)
      }
    }

    //遍历国内预定规则
    for(nbRule<-nbRuleList  if flag){
      if (requestUrl.matches(nbRule)){
        flag=false
        requestType=RequestType(FlightTypeEnum.National,BehaviorTypeEnum.Book)
      }
    }


    //遍历国际预定规则
    for(ibRule<-ibRuleList  if flag){
      if (requestUrl.matches(ibRule)){
        flag=false
        requestType=RequestType(FlightTypeEnum.International,BehaviorTypeEnum.Book)
      }
    }

    //数据没有匹配上任意一个规则
    if(flag){
      requestType=RequestType(FlightTypeEnum.Other,BehaviorTypeEnum.Other)
    }

    //3最终返回类型(国内查询    国内预定    国际查询   国际预定 )
    requestType

  }

}
