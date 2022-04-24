package iterable.demo01_iterable

//案例: 遍历集合元素.
object ClassDemo01 {
  def main(args: Array[String]): Unit = {
    //1. 定义一个列表,  存储1, 2, 3, 4, 5这五个数字.
    val list = (1 to 5).toList
    //2. 通过iterator()方法遍历上述的列表.
    //2.1 通过集合对象(list)来获取其对应的迭代器对象.
    val it: Iterator[Int] = list.iterator
    //2.2 判断是否还有下一个元素, 因为不知道集合中到底有多少个元素, 所以要采用循环来实现.
    while (it.hasNext) {
      //只要能走到这里, 说明集合中有元素.
      //2.3 通过next()方法来获取指定的元素.
      val num = it.next()
      println(num)
    }
    println("-" * 15)
    //3. 通过foreach()方法遍历上述的列表.
    //格式: list.foreach(函数对象)
    //3.1 普通版.
    list.foreach((x:Int) => println(x))
    println("-" * 15)
    //3.2 优化版.
    list.foreach(println(_))
  }
}
