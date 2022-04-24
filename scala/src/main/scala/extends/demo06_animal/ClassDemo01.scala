package `extends`.demo06_animal

//动物类案例.
object ClassDemo01 {
  //1. 定义抽象动物类(Animal), 属性: 姓名, 年龄, 行为: 跑步, 吃饭.
  abstract class Animal {
    //1.1 属性
    var name = ""
    var age = 0

    //1.2 行为
    //跑步
    def run() = println("动物会跑步!")

    //吃饭
    def eat()     //抽象方法
  }
  //2. 定义猫类(Cat)继承自动物类, 重写吃饭的方法, 并定义该类独有的抓老鼠的方法.
  class Cat extends Animal {
    //2.1 重写父类的eat()方法
    override def eat(): Unit = println("猫吃鱼")

    //2.2 抓老鼠的方法.
    def catchMouse() = println("猫会抓老鼠")
  }
  //3. 定义狗类(Dog)继承自动物类, 重写吃饭的方法, 并定义该类独有的看家的方法.
  class Dog extends Animal {
    //3.1 重写父类的eat()方法
    override def eat(): Unit = println("狗吃肉")

    //3.2 看家的方法.
    def lookHome() = println("狗会看家")
  }

  //4. 定义main方法, 作为程序的主入口.
  def main(args: Array[String]): Unit = {
    //5. 测试猫类.
    //5.1 创建猫类对象.
    val c:Animal = new Cat
    //5.2 给成员变量赋值.
    c.name = "加菲猫"
    c.age = 13
    //5.3 打印成员变量值.
    println(c.name, c.age)
    //5.4 调用从父类继承过来的方法: run(), eat()
    c.run()
    c.eat()
    //5.5 调用猫类独有的抓老鼠的功能.
    //c.catchMouse()
    if(c.isInstanceOf[Cat]) {
      //走这里, 说明c是猫类对象
      val c2 = c.asInstanceOf[Cat]
      c2.catchMouse()
    } else {
      //走这里, 说明c不是猫类对象.
      println("您传入的不是猫类对象")
    }

    //6. 测试狗类, 留给你自己实现.
  }
}
