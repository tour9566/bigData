package `package`.demo03_exercise

//案例: 计算器.
object ClassDemo01 {
  //1. 定义样例类Calculate, 表示计算器.
  case class Calculate(a:Int, b:Int) {
    //2. 定义四个方法, 分别表示: 加减乘除.
    //加法
    def add() = a + b
    //减法
    def subtract() = a - b
    //乘法
    def multiply() = a * b
    //除法
    def divide() = a / b
  }

  //3. 定义main方法, 作为程序的主入口.
  def main(args: Array[String]): Unit = {
    //4. 创建样例类Calculate类的对象, 并分别测试四个方法.
    val c = Calculate(10, 3)
    println("加法: " + c.add())         //13
    println("减法: " + c.subtract())    //7
    println("乘法: " + c.multiply())    //30
    println("除法: " + c.divide())      //3
  }
}
