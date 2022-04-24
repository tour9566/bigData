package oop.demo06_object

//案例: 演示如何创建单例对象.
object ClassDemo01 {

  //1. 定义单例对象Dog.
  object Dog {
    //2. 定义一个变量, 用来保存狗的腿的数量.
    val leg_num = 4
  }

  //3. 定义main方法, 作为程序的主入口.
  def main(args: Array[String]): Unit = {
    //4. 直接打印狗的腿的数量.
    println(Dog.leg_num)
  }
}
