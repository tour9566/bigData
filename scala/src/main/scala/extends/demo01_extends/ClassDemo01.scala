package `extends`.demo01_extends

//案例: 人类_非继承版.
object ClassDemo01 {
  //1. 定义老师类, 指定: 姓名和年龄, 吃饭行为.
  class Teacher {
    var name = ""
    var age = 0

    def eat() = println("老师喝牛肉汤!...")
  }
  //2. 定义学生类, 指定: 姓名和年龄, 吃饭的行为.
  class Student {
    var name = ""
    var age = 0

    def eat() = println("学生吃牛肉!...")
  }

  //3. 定义main方法, 作为程序的主入口.
  def main(args: Array[String]): Unit = {
    //4. 测试老师类.
    val t = new Teacher
    t.name = "刘老师"
    t.age = 33
    println(t.name, t.age)
    t.eat()
    println("-" * 15)

    //5. 测试学生类.
    val s = new Student
    s.name = "张三"
    s.age = 23
    println(s.name, s.age)
    s.eat()
  }
}
