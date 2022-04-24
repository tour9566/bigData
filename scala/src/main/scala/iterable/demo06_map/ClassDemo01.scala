package iterable.demo06_map

//案例: 演示Map集合.
object ClassDemo01 {
  def main(args: Array[String]): Unit = {
    //1. 定义Map集合, 存储数据为: "A" -> 1, "B" -> 2, "C" -> 3.
    val map = Map("A" -> 1, "B" -> 2, "C" -> 3)
    //2. 遍历Map集合.
    //方式一: 通过普通for循环实现.
    for((k, v) <- map) println(k, v)
    println("-" * 15)
    //方式二: 通过foreach函数实现.
    map.foreach(println(_))
    println("-" * 15)
    //3. 通过filterKeys()方法, 获取出键为"B"的这组键值对对象, 并打印结果.
    println(map.filterKeys(_ == "B"))
  }
}
