package `trait`.demo01_trait

//案例: 类继承单个特质.
object ClassDemo01 {
  //1. 定义特质Logger.
  trait Logger {
    def log(msg:String)   //抽象方法
  }

  //2. 定义类ConsoleLogger, 继承Logger特质.
  class ConsoleLogger extends Logger {
    override def log(msg: String): Unit = println(msg)
  }

  //3. 定义main方法, 作为程序的主入口.
  def main(args: Array[String]): Unit = {
    //4. 测试类中的成员方法.
    val cl = new ConsoleLogger
    cl.log("演示: 类继承单个特质")
  }
}
