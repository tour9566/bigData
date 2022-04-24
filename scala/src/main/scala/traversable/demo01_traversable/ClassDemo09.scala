package traversable.demo01_traversable

//案例: 集合类型转换.
object ClassDemo09 {
  def main(args: Array[String]): Unit = {
    //1. 定义Traversable集合t1, 包含1到5这五个数字.
    val t1 = Traversable(1, 2, 3, 4, 5)
    //2. 将t1集合分别转成数组(Array), 列表(List), 集(Set)这三种形式, 并打印结果.
    //2.1 把Traversable集合转换成 数组
    val arr = t1.toArray
    //2.2 把Traversable集合转换成 列表
    val list = t1.toList
    //2.3 把Traversable集合转换成 集
    val set = t1.toSet

    //3. 打印结果
    println(s"arr: ${arr}")       //输出语句直接打印数组对象, 打印的是地址值.
    println(s"list: ${list}")
    println(s"set: ${set}")
  }
}
