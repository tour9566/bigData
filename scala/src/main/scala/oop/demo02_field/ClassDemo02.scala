package oop.demo02_field

//案例: 演示通过下划线来初始化成员变量值.
object ClassDemo02 {

  //1. 定义Person类型.
  class Person {
    //2. 定义成员变量姓名和年龄.
    //方式一: 普通写法.
    //val name:String = ""
    //方式二: 采用类型推断实现.
    //val name = ""
    //方式三: 采用下划线来初始化成员变量值.
    //val name:String = _     //这样写会报错, 因为采用下划线来初始化成员变量值这种方式, 只针对于var类型的变量有效.
    var name:String = _
    var age:Int = _
  }

  //3. 定义main方法, 作为程序的主入口.
  def main(args: Array[String]): Unit = {
    //4. 创建Person类型的对象.
    val p = new Person
    //5. 给对象的成员变量赋值.
    p.name = "张三"
    p.age = 23
    //6. 打印结果.
    println(p.name, p.age)
  }


}
