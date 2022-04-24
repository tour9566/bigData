package com.air.antispider.stream.rulecompute.businessprocess

import java.text.SimpleDateFormat
import java.util

import com.air.antispider.stream.common.bean.{FlowCollocation, ProcessedData}
import org.apache.spark.streaming.Seconds
import org.apache.spark.streaming.dstream.DStream

import scala.collection.mutable.ArrayBuffer

//用于实现八个指标数据的计算

object CoreRule {

  //1 按 IP 段聚合  -  5  分钟内的  IP  段（IP  前两位）访问量
  def ipBlockCounts(processedData: DStream[ProcessedData]) = {

   val ipBlockCounts= processedData.map(message=>{
      //抽取出数据中的IP
     val ip= message.remoteAddr
      //截取出ip的前两位
      //192.168.56.151  192.168
      //获得到第一个.的位置
      val one =ip.indexOf(".")
      //获得到第二个.的位置
      val two =ip.indexOf(".",one+1)
      //ip段的截取
      val ipblock= ip.substring(0,two)
      //将前两位作为key, 1作为value,输出
      (ipblock,1)
    }).reduceByKeyAndWindow((a:Int,b:Int)=>a+b,Seconds(6),Seconds(2))//调用reduceByKeyAndWindow计算最终每个ip段的总量

    ipBlockCounts
  }

  //2 按 IP 地址聚合  -  某个  IP，5  分钟内总访问量
  def ipCounts(processedData: DStream[ProcessedData]) = {

    val ipCounts=processedData.map(message=>{
      //抽取出数据中的IP
      val ip =message.remoteAddr
      //将ip作为key, 1作为value,输出
      (ip,1)
    }).reduceByKeyAndWindow((a:Int,b:Int)=>a+b,Seconds(6),Seconds(2))//调用reduceByKeyAndWindow计算最终每个ip的总量

    ipCounts
  }

  //3 按 IP 地址聚合  -  某个  IP，5  分钟内的关键页面访问总量
  def criticalPagesCounts(processedData: DStream[ProcessedData], criticalPagesList: ArrayBuffer[String]) = {

    processedData.map(message=>{
      //抽取出数据中的IP 和数据中的url
      //获取ip
      val ip =message.remoteAddr
      //获取url
      val url=message.request

      //实例一个是否是关键页面的flag
      var flag=false

      //遍历数据库中的关键页面的规则。
      for(criticalPages<-criticalPagesList){
        //使用url与规则进行匹配，
        if (url.matches(criticalPages)){//匹配成功  表示数据是关键页面
          flag=true
        }else{//匹配不成功  表示数据不是关键页面
        }
      }

      if (flag){  //若数据的url能够与规则的任意一个匹配成功，那么表示这个url就是关键页面，将ip和1返回
        (ip,1)
      }else{      //若数据的url能够与规则的任意一个没有匹配成功，那么表示这个url不是关键页面，将ip和0返回
        (ip,0)
      }
    }).reduceByKeyAndWindow((a:Int,b:Int)=>a+b,Seconds(6),Seconds(2))  //调用reduceByKeyAndWindow计算最终每个ip段的总量

  }


  //4 按 IP 地址聚合  -  某个  IP，5  分钟内的  UA  种类数统计
  def userAgent(processedData: DStream[ProcessedData]) = {

    val ipAndUA=processedData.map(message=>{
      //抽取出数据中的IP 和数据中的ua
      //获取ip
      val ip =message.remoteAddr
      //获取ua
      val ua =message.httpUserAgent
      //使用ip 作为key ,ua作为value,将其输出
      (ip,ua)
    }).groupByKeyAndWindow(Seconds(6),Seconds(2))   //调用groupByKeyAndWindow，或得到的结果是key(IP)    和  value的list(ua的list)


    //	获取出ip ,和value的list(ua的list)，将value的list去重再求大小。
   val userAgent= ipAndUA.map(message=>{
      //获取ip
      val ip=message._1
      //获取ua的list
      val uaList=message._2
      //将value的list去重再求大小。
      val uaCounts= uaList.toList.distinct.size

      //将ip 和这个值大小返回
      (ip,uaCounts)
    })

    userAgent

  }


  //5 按 IP 地址聚合  -  某个  IP，5  分钟内查询不同行程的次数
  def differentJourneys(processedData: DStream[ProcessedData]) = {

   val ipAndDepArr= processedData.map(message=>{
      //获取ip
      val ip =message.remoteAddr
      //抽取出数据中的IP 和数据中的出发地和目的地
      //获取出发地
      val depCity=message.requestParams.depcity
      //获取目的地
      val arrCity=message.requestParams.arrcity
      //拼接出发地和目的地
      val deparrCity=depCity+arrCity
      //使用ip 作为key ,将出发地拼接目的地，以拼接的结果作为value,将其输出
      (ip,deparrCity)
    }).groupByKeyAndWindow(Seconds(6),Seconds(2))   //调用groupByKeyAndWindow，或得到的结果是key(IP)    和  value的list(出发地拼接目的地的list)


    val differentJourneysSize=ipAndDepArr.map(message=>{
      //获取ip
      val ip=message._1
      //获取不同行程的list
      // 获取出ip ,和value的list(出发地拼接目的地的list)，
      val deparrCity=message._2
      //将value的list去重再求大小。
     val  deparrSize=deparrCity.toList.distinct.size
      //将ip 和这个值大小返回
      (ip,deparrSize)

    })

    differentJourneysSize
  }


  //6 按 IP 地址聚合  -  某个  IP，5  分钟内访问关键页面的 Cookie 数
  def criticalCookies(processedData: DStream[ProcessedData], criticalPagesList: ArrayBuffer[String]) = {

   val ipAndCook= processedData.map(message=>{
      //1抽取出数据中的IP，数据中的url 和数据中的Cookie
      //获取ip
      val ip =message.remoteAddr
      //获取url
      val url=message.request
      //获取cook
      val cook=message.cookieValue_JSESSIONID



      //实例一个是否是关键页面的flag
      var flag=false

      //2遍历数据库中的关键页面的规则。
      for(criticalPages<-criticalPagesList){
        //3使用url与规则进行匹配，
        if (url.matches(criticalPages)){//匹配成功  表示数据是关键页面
          flag=true
        }else{//匹配不成功  表示数据不是关键页面
        }
      }

      if (flag){  //若数据的url能够与规则的任意一个匹配成功，那么表示这个url就是关键页面，，将ip和Cookie返回
        (ip,cook)
      }else{      //若数据的url能够与规则的任意一个没有匹配成功，那么表示这个url不是关键页面，将""和""返回
        ("","")
      }
    }).groupByKeyAndWindow(Seconds(6),Seconds(2))  //4调用groupByKeyAndWindow，或得到的结果是key(IP)    和  value的list(Cookie的list)


    //遍历ipAndCook
  val ipCookCount=  ipAndCook.map(message=>{
      //5获取出ip ,和value的list(Cookie的list)，将value的list去重再求大小。
      //获取ip
      val ip=message._1
      //获取Cookie的list
      val cookList=message._2
      //将value的list去重再求大小。
      val  cookSize=cookList.toList.distinct.size
      //6将ip 和这个值大小返回
      (ip,cookSize)

    })

    ipCookCount
  }

  // 7 按 IP 地址聚合  -  某个  IP，5  分钟内的关键页面最短访问间隔
  def criticalPagesMinTime(processedData: DStream[ProcessedData], criticalPagesList: ArrayBuffer[String]) = {


    val ipAndTime= processedData .map(message=>{
       //1 抽取出数据中的IP 和数据的访问时间及url
      //获取ip
      val ip =message.remoteAddr
      //获取url
      val url=message.request
      //获取time
      val time=message.timeIso8601

      //实例一个是否是关键页面的flag
      var flag=false

       //2 遍历数据库中的关键页面的规则。
      for(criticalPages<-criticalPagesList){
        //3使用url与规则进行匹配
        if (url.matches(criticalPages)){//若数据的url能够与规则的任意一个匹配成功，那么表示这个url就是关键页面
          flag=true
        }else{//若数据的url能够与规则的任意一个没有匹配成功，那么表示这个url不是关键页面
        }
      }

      if (flag){  //是关键页面，将ip和时间返回
        (ip,time)
      }else{      //不是关键页面，将“”和“”返回
        ("","")
      }
    }).groupByKeyAndWindow(Seconds(6),Seconds(2))  //4 调用groupByKeyAndWindow，或得到的结果是key(IP)    和  value的list(时间的list)


    //遍历ipAndTime
    val minTimeDiff=  ipAndTime.map(message=>{
      //5  获取到ip 和时间的list
      //获取ip
      val ip=message._1
      //获取时间的list
      val timeList=message._2
      //两个以上的数据才能计算时间差
      if(timeList.size>1){
        //实例一个接收时间戳类型数据的list
        val longTimeList=new util.ArrayList[Long]()

        //6 遍历时间的list ,将这个字符串类型国际时间转换成普通时间（时间戳）
        val simple =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        for(time<-timeList){
          //2019-05-14T09:08:50+08:00
          //2019-05-14 09:08:50
         val timeString= time.substring(0,time.indexOf("+")).replace("T"," ")
          //将String类型的时间转换成时间戳
          val longTime=simple.parse(timeString).getTime
          //7 将多个时间戳添加到一个list
          longTimeList.add(longTime)

        }

        //实例一个存储时间差的list
        val timeDiffList=new util.ArrayList[Long]()


        //8 对这个时间戳的list进行排序
        val longTimeListArray= longTimeList.toArray()
        util.Arrays.sort(longTimeListArray)
        //9  	遍历每两个时间戳，求取两个时间戳的时间差
        for(i<-1 until(longTimeListArray.length)){
          val timeDiff= longTimeListArray(i).toString.toLong-longTimeListArray(i-1).toString.toLong
          //10 将这个时间差添加到一个时间差的list
          timeDiffList.add(timeDiff)
        }

        //11 最时间差的list进行排序
       val timeDiffListArray= timeDiffList.toArray()
        util.Arrays.sort(timeDiffListArray)
        //12 获取排序后的第一个值就是最小时间差
        //13 将IP,和最小时间差返回
       (ip, timeDiffListArray(0).toString.toInt)
      }else{
        ("",0)
      }

    })

    minTimeDiff

  }



  // 8按 IP 地址聚合  -  某个  IP，  5  分钟内小于最短访问间隔（自设）的关键页面查询次数
  def lessDefaultTimes(processedData: DStream[ProcessedData], criticalPagesList: ArrayBuffer[String],
                       flowList: ArrayBuffer[FlowCollocation]) = {

    //实例最小时间间隔
    var defaultTime:Double=0

    //1  获取企业系统内的自定义的最小时间间隔
    for(flow<-flowList){
      //遍历流程，获取流程内的规则
      val rules=flow.rules
      //找到规则中ruleName等于"criticalPagesLessThanDefault"的规则（有两个参数，第一个是默认的最小时间，第二个是阈值）
        for(rule<-rules){
            if (rule.ruleName.equals("criticalPagesLessThanDefault")){
              //获取出规则种的第一个参数
              defaultTime= rule.ruleValue0*1000  //数据库配置的数据单位是秒    这里需要将其装换成毫秒
            }
        }

    }

    //2 设置一个计数器（记录小于自设值数据出现的次数）
    var lessDefaultTime=0



    //3 抽取出数据中的IP 和数据的访问时间  url
    val ipAndTime= processedData .map(message=>{
      //获取ip
      val ip =message.remoteAddr
      //获取url
      val url=message.request
      //获取time
      val time=message.timeIso8601

      //实例一个是否是关键页面的flag
      var flag=false

      //4 遍历数据库中的关键页面的规则。
      for(criticalPages<-criticalPagesList){
        //5 使用url与规则进行匹配，
        if (url.matches(criticalPages)){//若数据的url能够与规则的任意一个匹配成功，那么表示这个url就是关键页面
          flag=true
        }else{//若数据的url能够与规则的任意一个没有匹配成功，那么表示这个url不是关键页面
        }
      }

      if (flag){  //是关键页面，将ip和时间返回
        (ip,time)
      }else{      //不是关键页面，将“”和“”返回
        ("","")
      }
    }).groupByKeyAndWindow(Seconds(6),Seconds(2))      //6 调用groupByKeyAndWindow，或得到的结果是key(IP)    和  value的list(时间的list)



    //遍历ipAndTime
    val lessDefaultTimes=  ipAndTime.map(message=>{
      //7 获取到ip 和时间的list
      //获取ip
      val ip=message._1
      //获取时间的list
      val timeList=message._2
      //两个以上的数据才能计算时间差
      if(timeList.size>1){
        //实例一个接收时间戳类型数据的list
        val longTimeList=new util.ArrayList[Long]()

        //8 遍历时间的list ,将这个字符串类型国际时间转换成普通时间（时间戳）
        val simple =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        for(time<-timeList){
          //2019-05-14T09:08:50+08:00
          //2019-05-14 09:08:50
          val timeString= time.substring(0,time.indexOf("+")).replace("T"," ")
          //将String类型的时间转换成时间戳
          val longTime=simple.parse(timeString).getTime
          //9 将多个时间戳添加到一个list
          longTimeList.add(longTime)

        }

        //10 对这个时间戳的list进行排序
        val longTimeListArray= longTimeList.toArray()
        util.Arrays.sort(longTimeListArray)

        //11 遍历每两个时间戳，求取两个时间戳的时间差
        for(i<-1 until(longTimeListArray.length)){
          val timeDiff= longTimeListArray(i).toString.toLong-longTimeListArray(i-1).toString.toLong
          //12 用时间差与自设值进行对比，
          //若时间差小于自设值，那么计数器加1
          if(timeDiff<defaultTime){
            lessDefaultTime+=1
          }
          //若时间差大于自设值,跳过（什么都不做）
        }
        //13 将ip和计数器的值返回
        (ip,lessDefaultTime)


      }else{
        ("",0)
      }

    })

    lessDefaultTimes

  }
}
