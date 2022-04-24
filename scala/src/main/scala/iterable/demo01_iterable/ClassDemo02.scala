package iterable.demo01_iterable

//案例: 演示grouped分组功能.
object ClassDemo02 {
  def main(args: Array[String]): Unit = {
    //1. 定义一个Iterable集合, 存储1~13之间的所有整数.
    val list = (1 to 13).toIterable

    //2. 通过grouped()方法, 对Iterable集合按照5个元素为一组的形式进行分组, 遍历并打印结果.
    //方式一: 合并版.
    //2.1 获取迭代器对象.
    val it = list.grouped(5)
    //2.2 判断迭代器中是否有元素.
    while (it.hasNext) {
      //2.3 如果有, 则获取元素, 并打印.
      val result = it.next()
      println(result)
    }

    println("-" * 15)
    //方式二: 分解版.
    /*//第一次获取
    val b1 = it.hasNext
    if (b1) {
      val result1 = it.next()
      println(b1, result1)
    }
    //第二次获取
    val b2 = it.hasNext
    if (b2) {
      val result2 = it.next()
      println(b2, result2)
    }
    //第三次获取
    val b3 = it.hasNext
    if (b3) {
      val result3 = it.next()
      println(b3, result3)
    }
    //第四次获取
    val b4 = it.hasNext       //false
    //val result4 = it.next()   //如果迭代器中没有元素了, 你还调用next()方法获取元素, 则程序会抛异常.
    println(b4)*/
  }
}
