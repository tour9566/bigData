package oop.demo03_method

//案例: 演示如何在类中定义成员方法.
object ClassDemo01 {

  //1. 定义Customer类.
  class Customer {
    //2. 定义成员变量(姓名, 性别), 定义成员方法printHello()
    var name:String = _   //姓名
    var sex = ""          //性别

    //定义成员方法
    /*def printHello(msg:String) = {
      println(msg)
    }*/
    //简化版
    def printHello(msg:String) = println(msg)
  }

  //3. 定义main方法, 作为程序的主入口.
  def main(args: Array[String]): Unit = {
    //4. 创建Customer类的对象.
    val c = new Customer
    //5. 给成员变量赋值.
    c.name = "张三"
    c.sex = "男"
    //6. 打印成员变量值.
    println(c.name, c.sex)
    //7. 调用成员方法.
    c.printHello("Hello, Scala!")
  }
}
