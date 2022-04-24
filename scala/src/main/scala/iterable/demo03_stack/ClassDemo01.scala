package iterable.demo03_stack

import scala.collection.mutable

//案例: 演示Stack可变栈
object ClassDemo01 {
  def main(args: Array[String]): Unit = {
    //1. 定义可变栈Stack, 存储1到5这5个数字.
    val s1 = mutable.Stack(1, 2, 3, 4, 5)
    //2. 通过top()方法获取栈顶元素, 并打印.
    println(s1.top)
    //3. 通过push()方法把元素6添加到栈顶位置, 并打印.
    //细节: push()方法添加元素后, 返回集合本身.
    println(s1.push(6))           //6, 1, 2, 3, 4, 5
    //4. 通过pushAll()往栈顶位置添加Seq(11, 22, 33)序列, 并打印.
    //细节: pushAll()方法添加元素后, 返回集合本身.
    println(s1.pushAll(Seq(11, 22, 33)))      //33, 22, 11, 6, 1, 2, 3, 4, 5
    //5. 通过pop()方法移除栈顶元素, 并打印.
    //pop()方法: 移除栈顶元素, 并返回该元素.
    println(s1.pop())
    //6. 通过clear()方法移除集合内所有的元素.
    s1.clear()
    println(s1)
  }
}
