package iterable.demo02_seq

//案例: 修改指定的元素
object ClassDemo05 {
  def main(args: Array[String]): Unit = {
    //1. 定义Seq集合s1, 存储1到5这五个数据.
    val s1 = (1 to 5).toSeq
    //2. 修改索引2位置的元素值为: 10, 并打印结果.
    //第一个参数, 表示要修改的索引处的值,   第二个参数: 修改后的值.
    val s2 = s1.updated(2, 10)

    //3. 从索引1开始, 用子序列Seq(10, 20)替换3个元素, 并打印结果.
    /*
      第一个参数: 表示起始索引.
      第二个参数: 替换后的元素.
      第三个参数: 替换几个.
     */
    val s3 = s1.patch(1, Seq(10, 20), 3)

    //4. 并打印结果.
    println(s"s1: ${s1}")         //1, 2, 3, 4, 5
    println(s"s2: ${s2}")         //1, 2, 10, 4, 5
    println(s"s3: ${s3}")         //1, 10, 20, 5
  }
}
