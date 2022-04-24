//案例: 演示包的可见性.

//1. 定义父包com.itheima, 并在其中添加Employee类和子包scala.
package `package`.demo01_package

class Employee { //父包中的员工类
  //2. 在Employee类中定义两个变量(name, age), 及sayHello()方法.
  //private[com]解释: private的意思是: name这个成员变量只能在本类(Employee)中被直接访问, [com]的意思是: name成员变量可以在com包下的任意类中被访问.
  private var name = "张三"
  var age = 23

  private def sayHello() = println("Hello, Scala!")
}

  //3. 在子包com.itheima.scala中定义测试类, 创建Employee类对象, 并访问其成员.
  object ClassDemo04 {
    def main(args: Array[String]): Unit = {
      //创建Employee类对象, 并访问其成员.
//      val e = new Employee
//      println(e)
//      println(e.name, e.age)
//      e.sayHello()
    }
  }


