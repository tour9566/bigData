package oop.demo09_utils

import java.text.SimpleDateFormat
import java.util.Date

//案例: 定义日期工具类.
object ClassDemo01 {
  //1. 定义工具类DateUtils.
  object DateUtils {
    //2. 细节: 定义一个SimpleDateFormat类型的对象.
    var sdf:SimpleDateFormat = null

    //3. 定义date2String()方法, 用来将日期对象转换成其对应的字符串.
    def date2String(date:Date, template:String) = {
      sdf = new SimpleDateFormat(template)
      sdf.format(date)
    }
    //4. 定义string2Date()方法, 用来将字符串形式的日期转换成其对应的日期对象.
    def string2Date(dateString:String, template:String) = {
      sdf = new SimpleDateFormat(template)
      sdf.parse(dateString)
    }
  }

  //5. 定义main方法, 作为程序的主入口.
  def main(args: Array[String]): Unit = {
    //6. 测试date2String()方法.
    println(DateUtils.date2String(new Date(), "HH:mm:ss"))
    //7. 测试string2Date()方法.
    println(DateUtils.string2Date("1314年5月21日","yyyy年MM月dd日"))
  }
}
