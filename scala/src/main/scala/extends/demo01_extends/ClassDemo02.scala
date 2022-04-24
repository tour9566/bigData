package `extends`.demo01_extends

//案例: 人类_继承版.
object ClassDemo02 {

  //1. 定义Person类, 作为父类.
  class Person {
    //2. 定义公共的内容: 姓名, 年龄, 吃饭.
    var name = ""
    var age = 0

    def eat() = println("人要吃饭!...")
  }

  //3. 定义老师类, 继承人类.
  class Teacher extends Person{}

  //4. 定义学生类, 继承人类.
  class Student extends Person{}

  //5. 定义main方法, 作为程序的主入口.
  def main(args: Array[String]): Unit = {
    //6. 测试老师类.
    val t = new Teacher
    t.name = "刘老师"
    t.age = 33
    println(t.name, t.age)
    t.eat()
    println("-" * 15)

    //7. 测试学生类.
    val s = new Student
    s.name = "张三"
    s.age = 23
    println(s.name, s.age)
    s.eat()
  }
}
