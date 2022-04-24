package `trait`.demo06_exercise

//案例: 程序员案例
object ClassDemo01 {

  //1. 定义Programmer类, 表示所有的程序员.
  abstract class Programmer {
    //属性
    var name = ""
    var age = 0

    //行为
    def eat() //吃饭
    def skill() //技能
  }

  //2. 定义JavaProgrammer类, 表示Java程序员, 继承Programmer类.
  class JavaProgrammer extends Programmer {
    override def eat(): Unit = println("Java程序员吃大白菜, 喝大米粥.")

    override def skill(): Unit = println("我精通Java技术.")
  }

  //3. 定义PythonProgrammer类, 表示Python程序员, 继承Programmer类.
  class PythonProgrammer extends Programmer {
    override def eat(): Unit = println("Python程序员吃小白菜, 喝小米粥.")

    override def skill(): Unit = println("我精通Python技术")
  }

  //4. 定义BigData特质, 表示大数据技术.
  trait BigData {
    def learningBigData() = {
      println("来到黑马程序员之后: ")
      println("我学会了: Hadoop, Zookeeper, HBase, Hive, Sqoop, Scala, Flink, Spark等技术")
      println("我学会了: 企业级360°全方位用户画像, 千亿级数据仓, 黑马推荐系统, 电信信号强度诊断项目")
    }
  }

  //5. 定义PartJavaProgrammer类, 表示精通Java + 大数据技术的程序员, 继承JavaProgrammer, BigData特质.
  class PartJavaProgrammer extends JavaProgrammer with BigData {
    override def eat(): Unit = println("精通大数据 + Java技术的程序员, 吃牛肉, 喝牛奶!")

    override def skill(): Unit = {
      //自身技能
      super.skill()
      //来到黑马之后学到的技能
      learningBigData()
    }
  }

  //6. 定义PartPythonProgrammer类, 表示精通Python + 大数据技术的程序员, 继承PythonProgrammer, BigData特质.
  class PartPythonProgrammer extends PythonProgrammer with BigData {
    override def eat(): Unit = println("精通大数据 + Python技术的程序员, 吃羊肉, 喝羊奶!")

    override def skill(): Unit = {
      //自身技能
      super.skill()
      //来到黑马之后学到的技能
      learningBigData()
    }
  }

  //7. 定义main方法, 作为程序的主入口.
  def main(args: Array[String]): Unit = {
    //8. 测试.
    //8.1 测试普通的Java程序员.
    val jp = new JavaProgrammer
    jp.name = "张三"
    jp.age = 23
    println(jp.name, jp.age)
    jp.skill()
    jp.eat()
    println("-" * 15)
    //8.2 测试精通Java + 大数据的程序员.
    val pjp = new PartJavaProgrammer
    pjp.name = "黑马夯哥"
    pjp.age = 33
    println(pjp.name, pjp.age)
    pjp.skill()
    pjp.eat()

    //8.3 测试普通的Python程序员, 自己实现.
    //8.4 测试精通Python + 大数据的程序员, 自己实现.
  }
}
