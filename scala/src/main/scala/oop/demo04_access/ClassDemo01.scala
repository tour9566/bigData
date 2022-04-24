package oop.demo04_access

//案例: 演示访问权限修饰符: private, 默认.
object ClassDemo01 {

  //1. 定义一个Person类.
  class Person {
    //2. 定义私有的成员变量(姓名, 年龄)
    private var name:String = _
    private var age = 0

    //3. 定义成员方法.
    //获取姓名
    def getName() = name
    //设置姓名
    def setName(name:String) = this.name = name
    //获取年龄
    def getAge() = age
    //设置年龄
    def setAge(age:Int) = this.age = age
    //私有的, sayHello()打招呼的方法.
    private def sayHello() = println("Hello, Scala!")
  }


  //4. 定义main方法, 作为程序的主入口.
  def main(args: Array[String]): Unit = {
    //5. 创建Person类的对象.
    val p = new Person
    //6. 给成员变量赋值.
    //p.name = "张三"     //这样写会报错, 因为被private修饰的内容只能在本类中被直接访问.
    p.setName("张三")
    p.setAge(23)
    //7. 打印成员变量值.
    println(p.getName(), p.getAge())
    //8. 尝试调用Person类中的私有成员方法.
    //p.sayHello()       //这样写会报错, 因为被private修饰的内容只能在本类中被直接访问.
  }
}
