package `trait`.demo04_constructor

//案例: 演示trait的构造机制.
object ClassDemo01 {
  //1. 创建Logger特质，在构造器中打印"执行Logger构造器!"
  trait Logger {
    println("执行Logger构造器  2")
  }
  //2. 创建MyLogger特质，继承自Logger特质，，在构造器中打印"执行MyLogger构造器!"
  trait MyLogger extends Logger {
    println("执行MyLogger的构造器  3")
  }
  //3. 创建TimeLogger特质，继承自Logger特质，在构造器中打印"执行TimeLogger构造器!"
  trait TimeLogger extends Logger {
    println("执行TimeLogger的构造器 4")
  }
  //4. 创建Person类，在构造器中打印"执行Person构造器!"
  class Person {
    println("执行Person的构造器  1")
  }
  //5. 创建Student类，继承Person类及MyLogger, TimeLogge特质，在构造器中打印"执行Student构造器!"
  class Student extends Person with MyLogger with TimeLogger {
    println("执行Student类的构造器 5")
  }

  //6. 添加main方法，创建Student类的对象，观察输出。
  def main(args: Array[String]): Unit = {
    //创建Student类的对象，观察输出。
    /*
      执行顺序:
        1. 执行父类的构造器.
        2. 按照从左往右的顺序, 执行特质中的构造器.
        3. 当特质有父特质时, 会先执行父特质中的内容, 且父特质只执行一次.
        4. 最后在执行本类(子类)的构造器.
     */
    val s = new Student
  }
}
