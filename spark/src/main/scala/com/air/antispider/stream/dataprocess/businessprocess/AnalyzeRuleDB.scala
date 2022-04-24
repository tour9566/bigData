package com.air.antispider.stream.dataprocess.businessprocess

import java.sql.{Connection, PreparedStatement, ResultSet}
import java.util

import com.air.antispider.stream.common.bean.{AnalyzeRule, FlowCollocation, RuleCollocation}
import com.air.antispider.stream.common.util.database.{QueryDB, c3p0Util}
import com.air.antispider.stream.dataprocess.constants.{BehaviorTypeEnum, FlightTypeEnum}

import scala.collection.mutable.{ArrayBuffer, ListBuffer}

//用于实现在数据库中读取规到程序中
//本项目所有的数据库读取操作都放在这里
object AnalyzeRuleDB {

  //实现数据预处理阶段  数据过滤规则的读取
  def queryFilterRule(): ArrayBuffer[String] = {
    //读物数据过滤规则的sql
    val sql="select value from itcast_filter_rule"
    //接受数据的字段
    val field="value"
    //调用QueryDB读取数据
    val filterRuleList=QueryDB.queryData(sql,field)
    //返回过滤数据
    filterRuleList

  }



//实现数据分类规则的读取（四种规则，每种单独读取）
//operation_type操作类型: 0查询 1预订   flight_type航班类型: 0国内 1国际
//0                              0   国内查询
//0                              1   国际查询
//1                              0   国内预定
//1                              1   国际预定
  def queryRuleMap(): util.HashMap[String, ArrayBuffer[String]] = {

  //国内查询SQL
  val nqSQL="select expression from itcast_classify_rule where flight_type="+FlightTypeEnum.National.id+" and operation_type="+BehaviorTypeEnum.Query.id
  //国际查询SQL
  val iqSQL="select expression from itcast_classify_rule where flight_type="+FlightTypeEnum.International.id+" and operation_type="+BehaviorTypeEnum.Query.id
  //国内预定SQL
  val nbSQL="select expression from itcast_classify_rule where flight_type="+FlightTypeEnum.National.id+" and operation_type="+BehaviorTypeEnum.Book.id
  //国际预定SQL
  val ibSQL="select expression from itcast_classify_rule where flight_type="+FlightTypeEnum.International.id+" and operation_type="+BehaviorTypeEnum.Book.id

  //接受数据的字段
  val field="expression"

  //在数据库中读取数据
  //国内查询规则读取
 val nqRuleList= QueryDB.queryData(nqSQL,field)
  //国际查询规则读取
  val iqRuleList= QueryDB.queryData(iqSQL,field)
  //国内预定规则读取
  val nbRuleList= QueryDB.queryData(nbSQL,field)
  //国际预定规则读取
  val ibRuleList= QueryDB.queryData(ibSQL,field)

  //将四种业务的规则封装到一个Map内
  val RuleMaps=new  util.HashMap[String,ArrayBuffer[String]]()
  RuleMaps.put("nqRuleList",nqRuleList)
  RuleMaps.put("iqRuleList",iqRuleList)
  RuleMaps.put("nbRuleList",nbRuleList)
  RuleMaps.put("ibRuleList",ibRuleList)
  //将Map返回
  RuleMaps
}



  //实现数据解析的规则读取
  def queryRule(behaviorType: Int): List[AnalyzeRule] = {
    //mysql中解析规则（0-查询，1-预订）数据
    var analyzeRuleList = new ArrayBuffer[AnalyzeRule]()
    val sql: String = "select * from analyzerule where behavior_type =" + behaviorType
    var conn: Connection = null
    var ps: PreparedStatement = null
    var rs: ResultSet = null
    try {
      conn = c3p0Util.getConnection
      ps = conn.prepareStatement(sql)
      rs = ps.executeQuery()
      while (rs.next()) {
        val analyzeRule = new AnalyzeRule()
        analyzeRule.id = rs.getString("id")
        analyzeRule.flightType = rs.getString("flight_type").toInt
        analyzeRule.BehaviorType = rs.getString("behavior_type").toInt
        analyzeRule.requestMatchExpression = rs.getString("requestMatchExpression")
        analyzeRule.requestMethod = rs.getString("requestMethod")
        analyzeRule.isNormalGet = rs.getString("isNormalGet").toBoolean
        analyzeRule.isNormalForm = rs.getString("isNormalForm").toBoolean
        analyzeRule.isApplicationJson = rs.getString("isApplicationJson").toBoolean
        analyzeRule.isTextXml = rs.getString("isTextXml").toBoolean
        analyzeRule.isJson = rs.getString("isJson").toBoolean
        analyzeRule.isXML = rs.getString("isXML").toBoolean
        analyzeRule.formDataField = rs.getString("formDataField")
        analyzeRule.book_bookUserId = rs.getString("book_bookUserId")
        analyzeRule.book_bookUnUserId = rs.getString("book_bookUnUserId")
        analyzeRule.book_psgName = rs.getString("book_psgName")
        analyzeRule.book_psgType = rs.getString("book_psgType")
        analyzeRule.book_idType = rs.getString("book_idType")
        analyzeRule.book_idCard = rs.getString("book_idCard")
        analyzeRule.book_contractName = rs.getString("book_contractName")
        analyzeRule.book_contractPhone = rs.getString("book_contractPhone")
        analyzeRule.book_depCity = rs.getString("book_depCity")
        analyzeRule.book_arrCity = rs.getString("book_arrCity")
        analyzeRule.book_flightDate = rs.getString("book_flightDate")
        analyzeRule.book_cabin = rs.getString("book_cabin")
        analyzeRule.book_flightNo = rs.getString("book_flightNo")
        analyzeRule.query_depCity = rs.getString("query_depCity")
        analyzeRule.query_arrCity = rs.getString("query_arrCity")
        analyzeRule.query_flightDate = rs.getString("query_flightDate")
        analyzeRule.query_adultNum = rs.getString("query_adultNum")
        analyzeRule.query_childNum = rs.getString("query_childNum")
        analyzeRule.query_infantNum = rs.getString("query_infantNum")
        analyzeRule.query_country = rs.getString("query_country")
        analyzeRule.query_travelType = rs.getString("query_travelType")
        analyzeRule.book_psgFirName = rs.getString("book_psgFirName")
        analyzeRuleList += analyzeRule
      }
    } catch {
      case e: Exception => e.printStackTrace()
    } finally {
      c3p0Util.close(conn, ps, rs)
    }
    analyzeRuleList.toList
  }


  //实现历史出现过的黑名单读取到程序
  def getBiackIpDB() ={
    //读取黑名单的sql
    val sql="select ip_name from itcast_ip_blacklist"
    //接受数据的字段
    val field="ip_name"
    //调用QueryDB读取数据
    val blackIpList=QueryDB.queryData(sql,field)
    //返回过滤数据
    blackIpList
  }

//读取关键页面到程序
  def queryCriticalPages() = {
    //读取关键页面的sql
    val sql="select criticalPageMatchExpression from  itcast_query_critical_pages"
    //接受数据的字段
    val field="criticalPageMatchExpression"
    //调用QueryDB读取数据
    val criticalPagesList=QueryDB.queryData(sql,field)
    //返回过滤数据
    criticalPagesList

  }



  /**
    * 获取流程列表
    * 参数n为0为反爬虫流程
    *参数n为1为防占座流程
    * @return ArrayBuffer[FlowCollocation]
    */

  def createFlow(n:Int) :ArrayBuffer[FlowCollocation] = {
    var array = new ArrayBuffer[FlowCollocation]
    var sql:String = ""
    if(n == 0){ sql = "select itcast_process_info.id,itcast_process_info.process_name," +
      "itcast_strategy.crawler_blacklist_thresholds from itcast_process_info,itcast_strategy " +
      "where itcast_process_info.id=itcast_strategy.id and status=0"}



    else if(n == 1){sql = "select itcast_process_info.id,itcast_process_info.process_name,itcast_strategy.occ_blacklist_thresholds from itcast_process_info,itcast_strategy where itcast_process_info.id=itcast_strategy.id and status=1"}

    var conn: Connection = null
    var ps: PreparedStatement = null
    var rs:ResultSet = null
    try{
      conn = c3p0Util.getConnection
      ps = conn.prepareStatement(sql)
      rs = ps.executeQuery()
      while (rs.next()) {
        val flowId = rs.getString("id")
        val flowName = rs.getString("process_name")
        if(n == 0){
          val flowLimitScore = rs.getDouble("crawler_blacklist_thresholds")
          array += new FlowCollocation(flowId, flowName,createRuleList(flowId,n), flowLimitScore, flowId)
        }else if(n == 1){
          val flowLimitScore = rs.getDouble("occ_blacklist_thresholds")
          array += new FlowCollocation(flowId, flowName,createRuleList(flowId,n), flowLimitScore, flowId)
        }

      }
    }catch{
      case e : Exception => e.printStackTrace()
    }finally {
      c3p0Util.close(conn, ps, rs)
    }
    array
  }


  /**
    * 获取规则列表
    *
    * @param process_id 根据该ID查询规则
    * @return list列表
    */
  def createRuleList(process_id:String,n:Int):List[RuleCollocation] = {
    var list = new ListBuffer[RuleCollocation]
    val sql = "select * from(select itcast_rule.id,itcast_rule.process_id,itcast_rules_maintenance_table.rule_real_name," +
      "itcast_rule.rule_type,itcast_rule.crawler_type,itcast_rule.status,itcast_rule.arg0,itcast_rule.arg1," +
      "itcast_rule.score from itcast_rule,itcast_rules_maintenance_table where itcast_rules_maintenance_table."+
      "rule_name=itcast_rule.rule_name) as tab where process_id = '"+process_id + "'and crawler_type="+n
    //and status="+n
    var conn: Connection = null
    var ps: PreparedStatement = null
    var rs:ResultSet = null
    try{
      conn = c3p0Util.getConnection
      ps = conn.prepareStatement(sql)
      rs = ps.executeQuery()
      while ( rs.next() ) {
        val ruleId = rs.getString("id")
        val flowId = rs.getString("process_id")
        val rule_real_name = rs.getString("rule_real_name")
        val ruleType = rs.getString("rule_type")
        val ruleStatus = rs.getInt("status")
        val ruleCrawlerType = rs.getInt("crawler_type")
        val ruleValue0 = rs.getDouble("arg0")
        val ruleValue1 = rs.getDouble("arg1")
        val ruleScore = rs.getInt("score")
        val ruleCollocation = new RuleCollocation(ruleId,flowId,rule_real_name,ruleType,ruleStatus,ruleCrawlerType,ruleValue0,ruleValue1,ruleScore)
        list += ruleCollocation
      }
    }catch {
      case e : Exception => e.printStackTrace()
    }finally {
      c3p0Util.close(conn, ps, rs)
    }
    list.toList
  }



}
