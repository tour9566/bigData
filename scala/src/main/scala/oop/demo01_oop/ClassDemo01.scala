package oop.demo01_oop

//测试类, 来演示如何创建类和对象的.
object ClassDemo01 {

  //main方法, 作为程序的主入口, 所有代码的执行都是从这里开始的.
  def main(args: Array[String]): Unit = {
    //创建Person类型的对象.
    val p = new Person()
    //打印对象.
    println(p)
  }
}
