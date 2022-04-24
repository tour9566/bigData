package `extends`.demo03_change

//案例: 演示getClass和ClassOf关键字.
object ClassDemo02 {
  //1. 定义Person类.
  class Person
  //2. 定义Student类, 继承Person.
  class Student extends Person
  //3. 定义main方法, 作为程序的主入口.
  def main(args: Array[String]): Unit = {
    //4. 通过多态的形式创建Student类型的对象.
    val p:Person = new Student
    //5. 通过isInstanceOf关键字来判断创建的对象是否是Person类型的对象.
    println(p.isInstanceOf[Person])   //只要传入Person类型或者其子类对象, 返回值都是true.
    //6. 通过isInstanceOf关键字来判断创建的对象是否是Student类型的对象.
    println(p.isInstanceOf[Student])  //只要传入Student类型或者其子类对象, 返回值都是true.
    //7. 通过getClass和classOf关键字来判断创建的对象是否是Person类型的对象.
    println(p.getClass == classOf[Person])    //传入的必须是Person类型的对象, 否则返回false
    //8. 通过getClass和classOf关键字来判断创建的对象是否是Student类型的对象.
    println(p.getClass == classOf[Student])   //传入的必须是Student类型的对象, 否则返回false
  }
}
