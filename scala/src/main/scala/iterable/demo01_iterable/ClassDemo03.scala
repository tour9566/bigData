package iterable.demo01_iterable

//案例: 按照索引生成元组
object ClassDemo03 {
  def main(args: Array[String]): Unit = {
    //1. 定义一个Iterable集合, 存储"A", "B", "C", "D", "E"这五个字符串.
    val list1 = Iterable("A", "B", "C", "D", "E")
    //2. 通过zipWithIndex()方法, 按照 字符串->索引这种格式, 生成新的集合.
    //"A"->0, "B"->1, "C"->2, "D"->3, "E"->4
    val list2: Iterable[(String, Int)] = list1.zipWithIndex
    //3. 重新按照 索引->字符串这种格式, 生成新的集合.
    val list3: Iterable[(Int, String)] = list2.map(x => x._2 -> x._1)
    //4. 打印结果.
    list3.foreach(println(_))
  }
}
