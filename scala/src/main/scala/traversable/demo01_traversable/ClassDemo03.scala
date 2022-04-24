package traversable.demo01_traversable

//案例: 演示concat()方法, 拼接集合.
object ClassDemo03 {
  def main(args: Array[String]): Unit = {
    //1. 已知有三个Traversable集合, 分别存储(11, 22, 33), (44, 55), (66, 77, 88, 99)元素.
    val t1 = Traversable(11, 22, 33)
    val t2 = Traversable(44, 55)
    val t3 = Traversable(66, 77, 88, 99)

    //2. 通过concat()方法拼接上述的三个集合.
    val t4 = Traversable.concat(t1, t2, t3)

    //3. 将拼接后的结果打印到控制台上.
    println(s"拼接后的结果是: ${t4}")
  }
}
