package `extends`.demo01_extends

//案例: 演示单例对象继承.
object ClassDemo03 {
  //1. 定义Person类.
  class Person {
    var name = ""

    def sayHello() = println("hello, Scala!")
  }

  //2. 定义单例对象Student, 继承自Person.
  object Student extends Person{ }

  //3. 添加main方法, 作为程序的主入口.
  def main(args: Array[String]): Unit = {
    //4. 测试.
    Student.name = "张三"
    println(Student.name)
    Student.sayHello()
  }
}
