package `trait`.demo01_trait

//案例: 演示object单例对象继承特质
object ClassDemo03 {
  //1. 创建一个Logger特质，添加`log(msg:String)`方法
  trait Logger {
    def log(msg:String)
  }
  //2. 创建一个Warning特质, 添加`warn(msg:String)`方法
  trait Warning {
    def warn(msg:String)
  }
  //3. 创建一个单例对象ConsoleLogger，继承Logger和Warning特质, 重写特质中的抽象方法
  object ConsoleLogger extends Logger with Warning {
    override def log(msg: String): Unit = println(s"控制台日志信息: ${msg}")

    override def warn(msg: String): Unit = println(s"控制台警告信息: ${msg}")
  }

  //4. 编写main方法，调用单例对象ConsoleLogger的log和warn方法
  def main(args: Array[String]): Unit = {
    ConsoleLogger.log("我是一条普通的日志信息")
    ConsoleLogger.warn("我是一条警告信息")
  }
}
