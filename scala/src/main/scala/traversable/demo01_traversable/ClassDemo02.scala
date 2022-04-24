package traversable.demo01_traversable

//案例: 演示转置集合.
//细节: 集合的转置操作类似于列变行, 要求集合中每个集合元素的个数都相同.
object ClassDemo02 {
  def main(args: Array[String]): Unit = {
    //1. 定义一个Traversable集合t1, 它有三个元素, 每个元素都是Traversable集合, 并分别存储如下数据:
    //2. 第一个元素存储(1, 4, 7), 第二个元素存储(2, 5, 8), 第三个元素存储(3, 6, 9).
    val t1: Traversable[Traversable[Int]] = Traversable(Traversable(1, 4, 7), Traversable(2, 5, 8), Traversable(3, 6, 9))
    //3. 通过transpose方法, 对集合t1进行转置操作.
    val t2: Traversable[Traversable[Int]] = t1.transpose
    //4. 打印结果.
    /*
        转置之前      转置操作, 相当于列变行
        1 4 7         1 2 3
        2 5 8         4 5 6
        3 6 9         7 8 9
     */
    println(t2)
  }
}
