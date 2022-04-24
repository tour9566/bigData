package traversable.demo01_traversable

//案例: 通过scan()方法, 获取集合中元素的阶乘值.
//细节: scan()方法等价于scanLeft()方法, 还有一个和它相反的方法叫scanRight().
object ClassDemo05 {
  def main(args: Array[String]): Unit = {
    //1. 定义Traversable集合t1, 存储1, 2, 3, 4, 5这五个数字.
    val t1 = Traversable(1, 2, 3, 4, 5)
    //2. 假设初始值为1, 通过scan()方法, 分别获取t1集合中各个元素的阶乘值.
    //格式: t1.scan(初始化值)(函数对象)
    //方式一: 普通写法
    /*
      x: 表示前一个值的阶乘值.      例如: 1   1   2   6   24  120
      y: 表示要计算的下一个数据.    例如: 1   2   3   4   5
     */
    val t2 = t1.scan(1)((x:Int, y:Int) => x * y)

    //方式二: 合并版.
    val t3 = t1.scan(2)(_ * _)        //2   2   4   12   48  240

    //3. 打印结果.
    println(s"t2: ${t2}")
    println(s"t3: ${t3}")
  }
}
