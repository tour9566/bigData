package iterable.demo02_seq

//案例: 获取长度及元素
object ClassDemo02 {
  def main(args: Array[String]): Unit = {
    //1. 创建Seq集合, 存储元素1, 2, 3, 4, 5.
    val seq = (1 to 5).toSeq
    //2. 打印集合的长度.
    println(seq.length, seq.size)       //5, 5
    println("-" * 15)

    //3. 获取索引为2的元素.
    //3.1 方式一: 通过集合名(索引)的方式实现.
    println(seq(2))                   //3
    //3.2 方式二: 通过集合的伴生对象的apply()方法实现.
    println(seq.apply(2))             //3
  }
}
