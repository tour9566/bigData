package `trait`.demo03_pattern

//案例: 演示模板方法设计模式
object ClassDemo02 {
  //1. 定义一个模板类Template, 添加code()和getRuntime()方法, 用来获取某些代码的执行时间.
  abstract class Template {
    //用来记录具体要获取执行时间的代码.
    def code()

    //具体的计算规则.
    def getRuntime() = {
      //获取当前的毫秒值.
      val start = System.currentTimeMillis()
      //指定对应的代码.
      code()                //for(i <- 1 to 10000) println("Hello, Scala!")
      //获取当前的毫秒值.
      val end = System.currentTimeMillis()
      //两个毫秒值的差值, 就是代码的执行时间.
      end - start
    }
  }
  //2. 定义类ForDemo继承Template, 然后重写code()方法, 用来计算打印10000次"Hello,Scala!"的执行时间.
  class ForDemo extends Template {
    override def code(): Unit = for(i <- 1 to 10000) println("Hello, Scala!")
  }

  //3. 定义main方法, 用来测试代码的具体执行时间.
  def main(args: Array[String]): Unit = {
    //4. 创建ForDemo类的对象.
    val fd = new ForDemo
    //5. 调用ForDemo类从Template类中继承过来的getRuntime()方法.
    println(fd.getRuntime())
  }
}
