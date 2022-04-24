package `extends`.demo04_abstract

//案例: 抽象类入门
//细节: 抽象类的子类: 1. 如果也是抽象类, 则不用重写父类的抽象方法.   2. 如果是普通类, 则必须重写父类所有的抽象方法.
object ClassDemo01 {
  //1. 创建一个Shape抽象类，添加一个area抽象方法，用于计算面积
  abstract class Shape {
    //添加一个area抽象方法，用于计算面积
    def area():Double     //抽象方法
  }

  //2. 创建一个Square正方形类，继承自Shape，它有一个边长的主构造器，并实现计算面积方法
  class Square(var edge:Double) extends Shape {
    override def area(): Double = edge * edge
  }
  //3. 创建一个长方形类，继承自Shape，它有一个长、宽的主构造器，实现计算面积方法
  class Rectangle(var length:Double, var width:Double) extends Shape {
    override def area(): Double = length * width
  }
  //4. 创建一个圆形类，继承自Shape，它有一个半径的主构造器，并实现计算面积方法
  class Circle(var raius:Double) extends Shape {
    override def area(): Double = Math.PI * raius * raius
  }

  //5. 编写main方法，分别创建正方形、长方形、圆形对象，并打印它们的面积
  def main(args: Array[String]): Unit = {
    //测试正方形.
    val s1 = new Square(5)
    println(s"s1: ${s1.area()}")    //25

    //测试长方形
    val s2 = new Rectangle(4, 3)
    println(s"s2: ${s2.area()}")    //12

    //测试圆形.
    val s3 = new Circle(3)
    println(s"s3: ${s3.area()}")    //27
  }
}
