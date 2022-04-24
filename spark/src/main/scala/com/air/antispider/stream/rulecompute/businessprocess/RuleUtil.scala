package com.air.antispider.stream.rulecompute.businessprocess

import java.util.Date

import com.air.antispider.stream.common.bean.{FlowCollocation, ProcessedData}

import scala.collection.mutable.ArrayBuffer


//用于实现爬虫识别逻辑的代码
object RuleUtil {
  //爬虫识别




  def calculateAntiResult(message: ProcessedData, ip: String, ipBlockCountsMap: collection.Map[String, Int],
                          ipCountsMap: collection.Map[String, Int], criticalPagesCountsMap: collection.Map[String, Int],
                          userAgentCountsMap: collection.Map[String, Int], differentJourneysCountsMap: collection.Map[String, Int],
                          ipCookCountMap: collection.Map[String, Int], minTimeDiffMap: collection.Map[String, Int],
                          lessDefaultTimesMap: collection.Map[String, Int], flowList: ArrayBuffer[FlowCollocation]):AntiCalculateResult = {

    //1  指标碰撞   （准备数据）
    //192.168.56.151    -》  192.168
    val one =ip.indexOf(".")
    val two = ip.indexOf(".",one+1)
    //截取ip的段
    val ipBlock=ip.substring(0,two)


    //   1.1 提取一个ip在八个结果集内对应的数据，
    val  ipBlockCounts=ipBlockCountsMap.getOrElse(ipBlock,0)
    val  ipCounts=ipCountsMap.getOrElse(ip,0)
    val  criticalPagesCounts=criticalPagesCountsMap.getOrElse(ip,0)
    val  userAgentCounts=userAgentCountsMap.getOrElse(ip,0)
    val  differentJourneysCounts=differentJourneysCountsMap.getOrElse(ip,0)
    val  ipCookCount=ipCookCountMap.getOrElse(ip,0)
    val  minTimeDiff=minTimeDiffMap.getOrElse(ip,0)
    val  lessDefaultTimes=lessDefaultTimesMap.getOrElse(ip,0)

    //   1.2将取出的数据封装为Map,将Map与流程数据传入碰撞方法
    val dataParams=Map(
      "ipBlock"->ipBlockCounts,
      "ip"->ipCounts,
      "criticalPages"->criticalPagesCounts,
      "userAgent"->userAgentCounts,
      "criticalCookies"->ipCookCount,
      "flightQuery"->differentJourneysCounts,
      "criticalPagesAccTime"->minTimeDiff,
      "criticalPagesLessThanDefault"->lessDefaultTimes
    )


    //将Map与流程数据传入碰撞方法
    //1 指标碰撞（碰撞）
    val flowsScore: Array[FlowScoreResult]= calculateFlowsScore(dataParams,flowList)


    AntiCalculateResult(
    message: ProcessedData,
    ip: String,
    ipBlockCounts: Int,
    ipCounts: Int,
    criticalPagesCounts: Int,
    userAgentCounts: Int,
    minTimeDiff: Int,
    lessDefaultTimes: Int,
    differentJourneysCounts: Int,
    ipCookCount: Int,
    flowsScore: Array[FlowScoreResult])
}


  //1 指标碰撞（碰撞）
  def calculateFlowsScore(dataParams: Map[String, Int], flowList: ArrayBuffer[FlowCollocation]) = {
    val flowsScore =new  ArrayBuffer[FlowScoreResult]()

    //1 根据算法需求实例两个ArrayBuffer
      // 一个选定与否无关的数据分数，若数据的值大于设置的阈值那么返回数据的分数，反之返回0
      // 结果集的大小一直是8
      val allRuleScoreList=new ArrayBuffer[Double]()

      // 一个必须是选定状态的并且数据的结果大于配置的阈值返回分数,反之不做返回
      //结果集的大小不定
     val selectRuleScoreList=new ArrayBuffer[Double]()

    //命中的规则
    val hitRules=new ArrayBuffer[String]()

    //2 遍历流程数据，获取流程内的规则
    for(flow<-flowList){
      //获取流程内的规则（8个）
      val rules=flow.rules
      //遍历规则
      for(rule<-rules){
        //3 获取规则内的阈值    企业内配置的规则的阈值
        val databaseValue=if (rule.ruleName.equals("criticalPagesLessThanDefault")) rule.ruleValue1 else rule.ruleValue0
        //4 获取数据计算出的结果
        val dataValue= dataParams.getOrElse(rule.ruleName,0)

        //5 数据对比
        //数据计算的结果大于阈值，根据需求将分数写入相应的ArrayBuffer内
        //数据计算的结果小于阈值，根据需求填入ArrayBuffer（是否需要填写）
        if (dataValue>databaseValue){
          allRuleScoreList.append(rule.ruleScore)
          hitRules.append(rule.ruleName)
          //判断规则是否选中
          if(rule.ruleStatus==0){
            selectRuleScoreList.append(rule.ruleScore)
          }
        }else{//反之  数据计算的结果小于阈值
          allRuleScoreList.append(0)
        }
      }


      //2  最终的打分
     val flowScore= computeScore(allRuleScoreList.toArray,selectRuleScoreList.toArray)
      if (flowScore>0){
          println(">>>>>------> 数据得分："+flowScore)
      }

      //3  爬虫判断（结论：是/不是）
        //3.1  将获取流程中最终的阈值（爬虫与非爬虫的分界点）
         val flowLimitScore= flow.flowLimitScore

        //3.2  用数据得分与最终阈值对比
           //若数据得分大于最终阈值，那么次数据是爬虫，返回True
           //若数据得分小于最终阈值，那么次数据不是爬虫，返回False
         val isUpLimited=if(flowScore>flowLimitScore)true else false
          if(isUpLimited){
            println(">>>>>=======>      True")
          }


      flowsScore.append(FlowScoreResult(flow.flowId,flowScore,flowLimitScore,isUpLimited,flow.strategyCode,hitRules.toList,new Date().toString))


    }

    flowsScore.toArray
  }






  /**
    * 通过算法计算打分
    * 系数2权重：60%，数据区间：10-60
    * 系数3权重：40，数据区间：0-40
    * 系数2+系数3区间为：10-100
    * 系数1为:平均分/10
    * 所以，factor1 * (factor2 + factor3)区间为:平均分--10倍平均分
    * @return
    */

  def computeScore(scores: Array[Double],xa: Array[Double]): Double = {
    //打分列表
    //总打分
    val sum = scores.sum
    //打分列表长度
    val dim = scores.length
    //系数1：平均分/10
    val factor1 = sum / (10 * dim)
    //命中数据库开放规则的score
    //命中规则中，规则分数最高的
    val maxInXa = if (xa.isEmpty) {
      0.0
    } else {
      xa.max
    }
    //系数2：系数2的权重是60，指的是最高score以6为分界，最高score大于6，就给满权重60，不足6，就给对应的maxInXa*10
    val factor2 = if (1 < (1.0 / 6.0) * maxInXa) {
      60
    } else {
      (1.0 / 6.0) * maxInXa * 60
    }
    //系数3：打开的规则总分占总规则总分的百分比，并且系数3的权重是40
    val factor3 = 40 * (xa.sum / sum)
    /**
      * 系数2权重：60%，数据区间：10-60
      * 系数3权重：40，数据区间：0-40
      * 系数2+系数3区间为：10-100
      * 系数1为:平均分/10
      * 所以，factor1 * (factor2 + factor3)区间为:平均分--10倍平均分
      */
    factor1 * (factor2 + factor3)
  }




}
