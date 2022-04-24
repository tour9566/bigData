package oop.demo08_companion

//案例: 演示private[this]访问权限修饰符.
//解释: private[this]修饰的内容只能在本类中直接使用, 就连伴生类(或者伴生对象)也无法直接访问.
object ClassDemo02 {

  //1. 定义Person伴生类, 并在其中定义一个name字段.
  class Person(private var name: String) {
  //class Person(private[this] var name: String) {      //这样写, 第16行代码会报错.

  }

  //2. 定义Person类的伴生对象.
  object Person {
    //3. 在其中定义一个printPerson()方法, 用来打印某个Person对象的属性值.
    def printPerson(p: Person) = println(p.name)
  }

  //4. 定义main方法, 作为程序的主入口.
  def main(args: Array[String]): Unit = {
    //5. 调用伴生对象中的printPerson()方法, 尝试是否可以访问private[this]修饰的内容.
    val p = new Person("张三")
    Person.printPerson(p)
  }

}
