package oop.demo06_object

//案例: 演示如何在单例对象中定义成员方法.
object ClassDemo02 {

  //1. 定义单例对象.
  object PrintUtil {
    //2. 在单例对象中定义成员方法, 用来打印分割线.
    def printSpliter() = println("-" * 15)
  }

  //3. 定义main方法, 作为程序的主入口.
  def main(args: Array[String]): Unit = {
    //4. 调用方法, 打印分割线.
    PrintUtil.printSpliter()
  }
}
