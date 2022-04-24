package oop.demo08_companion

//案例: 演示如何定义伴生对象
object ClassDemo01 {

  //1. 定义伴生类Generals.
  class Generals { //里边写的内容都是非静态的.
    //2. 定义toWar()方法, 表示打仗.
    def toWar() = println(s"武将拿着${Generals.armsName}武器, 上阵杀敌!")
  }

  //3. 定义伴生对象.
  object Generals { //里边写的内容都是静态的.
    //4. 定义一个私有的成员变量, 用来保存武器的名称.
    private val armsName = "青龙偃月刀"
  }

  //5. 定义main方法, 作为程序的主入口.
  def main(args: Array[String]): Unit = {
    //6. 创建伴生类的对象.
    val c = new Generals
    //7. 调用toWar()方法.
    c.toWar()
  }
}
