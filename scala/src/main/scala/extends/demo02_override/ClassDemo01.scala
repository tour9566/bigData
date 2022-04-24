package `extends`.demo02_override

//案例: 演示方法重写.
object ClassDemo01 {

  //1. 定义Person类.
  class Person {
    //成员变量
    val name = "张三" //val修饰的变量, 值不能被修改.
    var age = 23 //var修饰的变量, 值可以被修改.

    //成员方法
    def sayHello() = println("Hello, Person!")
  }

  //2. 定义Student类, 继承Person.
  class Student extends Person {
    //重写父类的成员变量.
    override val name = "李四"
    //override var age = 30       //这样写会报错, 因为父类中用var修饰的变量, 子类不能重写.

    //重写父类的成员方法.
    override def sayHello() = {
      //调用父类的成员方法
      super.sayHello()
      println("Hello, Student!")
    }
  }

  //3. 定义main方法.
  def main(args: Array[String]): Unit = {
    //4. 测试.
    val s = new Student
    println(s.name, s.age)      //李四, 23
    s.sayHello()
  }

}
