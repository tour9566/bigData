package `extends`.demo03_change

//案例: 演示isInstanceOf和asInstanceOf关键字.
object ClassDemo01 {

  //1. 定义Person类.
  class Person

  //2. 定义Student, 继承自Person. 该类有一个自己的成员方法: sayHello().
  class Student extends Person {
    def sayHello() = println("Hello, Student!")
  }

  //3. 定义main方法, 作为程序的主入口.
  def main(args: Array[String]): Unit = {
    //4. 通过多态的形式, 创建Student类型的对象.
    /*
      多态:
        概述: 同一个事物在不同时刻表现出来的不同形态, 状态.
        弊端: 父类引用不能直接使用子类的特有成员.
     */
    val p: Person = new Student //多态.
    //5. 尝试调用Student类中的sayHello()方法.
    //p.sayHello()      //这行代码会报错, 因为父类引用不能直接使用子类的特有成员.

    //6. 解决方案.
    //6.1 判断对象p是否是Student类型的对象.
    if (p.isInstanceOf[Student]) {
      //6.2 如果是, 就将其转换成Student类型的对象, 然后调用sayHello()方法即可.
      val s = p.asInstanceOf[Student]
      //上边这行代码相当于: val s:Student = new Student
      s.sayHello()
    }
  }
}
