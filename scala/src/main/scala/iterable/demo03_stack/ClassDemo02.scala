package iterable.demo03_stack

import scala.collection.mutable

//案例: 演示ArrayStack可变栈
object ClassDemo02 {
  def main(args: Array[String]): Unit = {
    //1. 定义可变栈ArrayStack, 存储1到5这5个数字.
    val s1 = mutable.ArrayStack(1, 2, 3, 4, 5)
    //2. 通过dup()方法复制栈顶元素, 并打印结果.
    s1.dup()
    println(s"s1: ${s1}")
    println("-" * 15)

    //3. 通过preserving()方法实现`先清空集合元素, 然后恢复集合中清除的数据`, 并打印.
    s1.preserving({
      //当该方法中的内容执行完毕后, 栈中的数据会恢复到该方法执行之前的状态.
      s1.clear()
      println("看看我执行了吗? ")
    })

    println(s"s1: ${s1}")
  }
}
