package oop.demo01_oop

//案例: 演示创建类和对象的简写形式.
object ClassDemo03 {
  //1. 创建Person类型.
  class Person

  //2. 创建main方法作为程序的主入口.
  def main(args: Array[String]): Unit = {
    //3. 创建Person类型的对象.
    val p = new Person
    //4. 打印对象.
    println(p)
  }

}
