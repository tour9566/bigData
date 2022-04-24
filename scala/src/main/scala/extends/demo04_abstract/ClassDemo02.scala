package `extends`.demo04_abstract

//案例: 演示抽象字段.
object ClassDemo02 {
  //1. 创建一个Person抽象类，它有一个String抽象字段occupation
  abstract class Person {
    val occupation:String   //抽象字段
  }
  //2. 创建一个Student类，继承自Person类，重写occupation字段，初始化为学生
  class Student extends Person {
    override val occupation: String = "学生"
  }
  //3. 创建一个Teacher类，继承自Person类，重写occupation字段，初始化为老师
  class Teacher extends Person {
    override val occupation: String = "老师"
  }

  //4. 添加main方法，分别创建Student/Teacher的实例，然后分别打印occupation
  def main(args: Array[String]): Unit = {
    //测试老师类.
    val t = new Teacher
    println(t.occupation)
    //测试学生类.
    val s = new Student
    println(s.occupation)
  }
}
