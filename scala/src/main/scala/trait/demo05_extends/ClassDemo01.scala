package `trait`.demo05_extends

//案例: 演示特质继承类
object ClassDemo01 {
  //1. 定义Message类. 添加printMsg()方法, 打印"学好Scala, 走到哪里都不怕!"
  class Message {
    def printMsg() = println("学好Scala, 走到哪里都不怕!")
  }

  //2. 创建Logger特质，继承Message类.
  trait Logger extends Message        //此时, Logger特质已经从Message类中继承过来了: printMsg()方法.

  //3. 定义ConsoleLogger类, 继承Logger特质.
  class ConsoleLogger extends Logger  //此时, ConsoleLogger类已经从Logger特质中继承过来了: printMsg()方法.

  //4. 在main方法中, 创建ConsoleLogger类的对象, 并调用printMsg()方法.
  def main(args: Array[String]): Unit = {
    //创建ConsoleLogger类的对象, 并调用printMsg()方法.
    val cl = new ConsoleLogger
    cl.printMsg()
  }
}
