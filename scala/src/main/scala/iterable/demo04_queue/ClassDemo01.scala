package iterable.demo04_queue

import scala.collection.mutable

//案例: 演示Queue序列.
object ClassDemo01 {
  def main(args: Array[String]): Unit = {
    //1. 定义可变队列Queue, 存储1到5这五个数据.
    val q1 = mutable.Queue(1, 2, 3, 4, 5)
    //2. 往队列中添加元素6, 并打印.
    q1.enqueue(6)
    //3. 往队列中添加元素7, 8, 9, 并打印.
    q1.enqueue(7, 8, 9)
    //4. 移除队列的第一个元素, 并打印该元素.
    println(q1.dequeue())       //1
    //5. 移除队列的第一个奇数, 并打印该元素.
    println(q1.dequeueFirst(_ % 2 != 0))    //3
    //6. 移除队列中所有的偶数, 并打印所有被移除的数据.
    println(q1.dequeueAll(_ % 2 == 0))      //2, 4, 6, 8
    //7. 打印可变队列Queue, 查看最终结果.
    println(q1)     //5, 7, 9
  }
}
