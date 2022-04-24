package `extends`.demo05_inner

//案例: 演示匿名内部类.
/*
  使用场景:
    1. 当对成员方法仅调用一次的时候.
    2. 可以作为方法的实际参数进行传递.
 */
object ClassDemo01 {

  //1. 创建一个Person抽象类，并添加一个sayHello抽象方法
  abstract class Person {
    def sayHello() //抽象方法
  }

  //2. 定义一个show()方法, 该方法需要传入一个Person类型的对象, 然后调用Person类中的sayHello()方法.
  def show(p:Person) = p.sayHello()

  //3. 添加main方法，通过匿名内部类的方式来创建Person类的子类对象, 调用Person类的sayHello()方法.
  def main(args: Array[String]): Unit = {
    //需求: 调用Person类中的sayHello()方法.
    new Person() {
      override def sayHello(): Unit = println("Hello, Scala!")
    }.sayHello()

    //4. 调用show()方法.
    //show(这里要传入一个Person类型的对象)
    //通过匿名内部类的形式创建Person抽象类的子类对象.
    val p:Person = new Person() {   //多态
      override def sayHello(): Unit = println("Hello, Scala, 我是通过匿名内部类的方式实现的!")
    }
    show(p)
  }
}
