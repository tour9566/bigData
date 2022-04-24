package iterable.demo02_seq

//案例: 创建Seq对象.
object ClassDemo01 {
  def main(args: Array[String]): Unit = {
    //创建Seq集合, 存储元素1, 2, 3, 4, 5,
    val seq = (1 to 5).toSeq
    //并打印结果.
    println(seq)
  }
}
