package traversable.demo01_traversable

//案例: 通过偏函数筛选出集合中所有的偶数.
object ClassDemo04 {
  def main(args: Array[String]): Unit = {
    //1. 已知有一个Traversable集合, 存储元素为: 1, 2, 3, 4, 5, 6, 7, 8, 9, 10.
    val t1 = (1 to 10).toTraversable                        //底层是Vector
    val t2 = Traversable(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)     //底层是List

    //2. 通过collect方法筛选出集合中所有的偶数.
    //格式: val t3 = t1.collect(偏函数对象)
    //方式一: 分解版
    //2.1 定义偏函数, 用来过滤偶数.
    val pf:PartialFunction[Int, Int] = {
      //x表示集合中的每一个元素: 例如: 1, 2, 3, 4, 5, 6, 7, 8, 9, 10
      case x if x % 2 == 0 => x
    }
    //2.2 调用collect()方法, 获取t1集合中所有的偶数.
    val t3 = t1.collect(pf)

    //方式二: 合并版.
    val t4 = t2.collect({
      //x表示集合中的每一个元素: 例如: 1, 2, 3, 4, 5, 6, 7, 8, 9, 10
      case x if x % 2 == 0 => x
    })

    //3. 打印结果.
    println(s"t3: ${t3}")           //底层是Vector
    println(s"t4: ${t4}")           //底层是List
  }
}
