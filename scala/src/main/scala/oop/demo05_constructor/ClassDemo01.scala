package oop.demo05_constructor

//案例: 演示主构造器.
object ClassDemo01 {

  //1. 定义Person类型, 在主构造器中指定: 姓名, 年龄.
  class Person(val name: String = "张三", val age: Int = 23) {
    //2. 在主构造器中输出"调用主构造器了"
    println("调用主构造器了!")
  }

  //3. 定义main方法, 作为程序的主入口.
  def main(args: Array[String]): Unit = {
    //4. 创建空对象, 并打印属性值.
    val p1 = new Person()
    println(s"p1: ${p1.name}, ${p1.age}")   //张三, 23
    //5. 创建全参对象, 并打印属性值.
    val p2 = new Person("李四", 24)
    println(s"p2: ${p2.name}, ${p2.age}")   //李四, 24
    //6. 创建对象, 仅指定年龄, 并打印属性值.
    val p3 = new Person(age = 30)
    println(s"p3: ${p3.name}, ${p3.age}")   //张三, 30
  }
}
