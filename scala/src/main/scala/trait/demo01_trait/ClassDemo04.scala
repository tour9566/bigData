package `trait`.demo01_trait

//案例: 演示特质中的成员
object ClassDemo04 {
  //1. 定义一个特质Hero, 添加具体字段name(姓名), 抽象字段arms(武器), 具体方法eat(), 抽象方法toWar()
  trait Hero {
    //具体字段
    var name = "关羽"
    //抽象字段
    var arms:String
    //具体方法
    def eat() = println("吃肉喝酒, 养精蓄锐!")
    //抽象方法
    def toWar()
  }
  //2. 定义一个类Generals, 继承Hero特质, 重写其中所有的抽象成员.
  class Generals extends Hero {
    override var arms: String = "青龙偃月刀"

    override def toWar(): Unit = println(s"武将${name}带着武器${arms}, 上阵杀敌!")
  }

  //3. 在main方法中, 创建Generals类的对象, 调用其中的成员.
  def main(args: Array[String]): Unit = {
    //创建Generals类的对象
    val g = new Generals
    //调用其中的成员.
    g.eat()
    g.toWar()
  }
}
