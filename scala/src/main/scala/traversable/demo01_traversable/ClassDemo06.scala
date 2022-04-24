package traversable.demo01_traversable

//案例: 获取Traversable对象的特定的元素
object ClassDemo06 {
  def main(args: Array[String]): Unit = {
    //1. 定义一个Traversable集合, 包含1, 2, 3, 4, 5, 6这六个元素.
    val t1 = Traversable(1, 2, 3, 4, 5, 6)
    //2. 分别通过head, headOption, last, lastOption获取集合中的首尾第一个元素, 并打印.
    println(t1.head)
    println(t1.last)
    println(t1.headOption)
    println(t1.lastOption)
    println("-" * 15)
    //3. 通过find方法获取集合中第一个偶数, 并打印.
    println(t1.find(_ % 2 == 0))
    println("-" * 15)
    //4. 通过slice()方法获取3, 4, 5这三个元素, 并将它们放到一个新的Traversable集合中, 然后打印结果.
    val t2 = t1.slice(2, 5)   //包左不包右
    println(s"t2: ${t2}")
  }
}
