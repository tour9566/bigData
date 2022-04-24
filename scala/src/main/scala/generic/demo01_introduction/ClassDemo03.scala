package generic.demo01_introduction

//案例: 演示泛型特质.
//泛型特质: 在定义其子类或者子单例对象的时候, 明确具体的数据类型.
object ClassDemo03 {
  //1. 定义泛型特质Logger, 该类有一个变量a和show()方法, 它们都是用Logger特质的泛型.
  trait Logger[T] {
    //成员变量
    val a:T
    //成员方法
    def show(b:T)
  }

  //2. 定义单例对象ConsoleLogger, 继承Logger特质.
  object ConsoleLogger extends Logger[String]{
    override val a: String = "刘亦菲"

    override def show(b: String): Unit = println(b)
  }

  def main(args: Array[String]): Unit = {
    //3. 打印单例对象ConsoleLogger中的成员.
    println(ConsoleLogger.a)
    ConsoleLogger.show("abc")
  }
}
