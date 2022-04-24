package com.air.antispider.stream.dataprocess.businessprocess
import java.util.regex.Pattern
import com.air.antispider.stream.common.util.decode.{RequestDecoder, EscapeToolBox}
import com.air.antispider.stream.common.util.jedis.PropertiesUtil
import com.air.antispider.stream.common.util.string.CsairStringUtils

//用于数据切分的实现过程
object DataSplit {
  //实现数据的拆分
  def dataSplit(message: String): (String, String, String, String, String, String, String, String, String, String, String, String)  = {

    //用#CS#分割数据
    val values = message.split("#CS#", -1)
    //记录数据长度
    val valuesLength = values.length

    //request原始数据
    val request = if (valuesLength > 1) values(1) else ""
    //分割出request中的url
    val requestUrl = if (request.split(" ").length > 1) {
      request.split(" ")(1)
    } else {
      ""
    }
    //请求方式GET/POST
    val requestMethod = if (valuesLength > 2) values(2) else ""
    //content_type
    val contentType = if (valuesLength > 3) values(3) else ""
    //Post提交的数据体
    val requestBody = if (valuesLength > 4) values(4) else ""
    //http_referrer
    val httpReferrer = if (valuesLength > 5) values(5) else ""
    //客户端IP
    val remoteAddr = if (valuesLength > 6) values(6) else ""
    //客户端UA
    val httpUserAgent = if (valuesLength > 7) values(7) else ""
    //服务器时间的ISO8610格式
    val timeIso8601 = if (valuesLength > 8) values(8) else ""
    //服务器地址
    val serverAddr = if (valuesLength > 9) values(9) else ""
    //Cookie信息
    //原始信息中获取Cookie字符串，去掉空格，制表符
    val cookiesStr = CsairStringUtils.trimSpacesChars(if (valuesLength > 10) values(10) else "")
    //提取Cookie信息并保存为K-V形式
    val cookieMap = {
      var tempMap = new scala.collection.mutable.HashMap[String, String]
      if (!cookiesStr.equals("")) {
        cookiesStr.split(";").foreach { s =>
          val kv = s.split("=")
          //UTF8解码
          if (kv.length > 1) {
            try {
              val chPattern = Pattern.compile("u([0-9a-fA-F]{4})")
              val chMatcher = chPattern.matcher(kv(1))
              var isUnicode = false
              while (chMatcher.find()) {
                isUnicode = true
              }
              if (isUnicode) {
                tempMap += (kv(0) -> EscapeToolBox.unescape(kv(1)))
              } else {
                tempMap += (kv(0) -> RequestDecoder.decodePostRequest(kv(1)))
              }
            } catch {
              case e: Exception => e.printStackTrace()
            }
          }
        }
      }
      tempMap
    }
    //Cookie关键信息解析
    //从配置文件读取Cookie配置信息
    val cookieKey_JSESSIONID = PropertiesUtil.getStringByKey("cookie.JSESSIONID.key", "cookieConfig.properties")
    val cookieKey_userId4logCookie = PropertiesUtil.getStringByKey("cookie.userId.key", "cookieConfig.properties")
    //Cookie-JSESSIONID
    val cookieValue_JSESSIONID = cookieMap.getOrElse(cookieKey_JSESSIONID, "NULL")
    //Cookie-USERID-用户ID
    val cookieValue_USERID = cookieMap.getOrElse(cookieKey_userId4logCookie, "NULL")



    (requestUrl,requestMethod,contentType,requestBody,httpReferrer,remoteAddr,httpUserAgent,timeIso8601,serverAddr,cookiesStr,cookieValue_JSESSIONID,cookieValue_USERID)


  }

}
