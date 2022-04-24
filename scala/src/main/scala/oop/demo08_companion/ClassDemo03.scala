package oop.demo08_companion

//案例: 演示apply()方法, 实现创建对象的时候"免new"
object ClassDemo03 {

  //1. 定义Person类, 属性: 姓名, 年龄.
  class Person(var name: String = "", var age: Int = 0) {}

  //2. 定义Person类的伴生对象.
  object Person {
    //3. 在其中定义apply()方法, 用来实现创建Person对象的时候 免new.
    //apply()方法, Scala SDK会自动调用
    def apply(name:String, age:Int) = new Person(name, age)
  }

  //4. 定义main方法, 作为程序的主入口.
  def main(args: Array[String]): Unit = {
    //5. 创建Person类型的对象.
    val p = Person("张三", 23)
    //6. 打印对象的属性值.
    println(p.name, p.age)
  }
}
