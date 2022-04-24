package traversable.demo01_traversable

//案例: 演示聚合操作.
object ClassDemo08 {
  def main(args: Array[String]): Unit = {
    //1. 定义Traversable集合t1, 包含1到6这六个数字.
    val t1 = Traversable(1, 2, 3, 4, 5, 6)
    //2. 通过count()方法统计t1集合中所有奇数的个数, 并打印结果.
    println("奇数的个数是: " + t1.count(_ % 2 != 0))            //3
    //通过filter()方法来实现, 但是这里不推荐, 因为该方法会返回一个集合对象.
    println("奇数的个数是: " + t1.filter(_ % 2 != 0).size)      //3
    println("-" * 15)
    //3. 通过sum()方法获取t1集合中所有的元素和,  并打印结果.
    println("所有的元素和是: " + t1.sum)                       //21
    //4. 通过product()方法获取t1集合中所有的元素乘积,  并打印结果.
    println("所有的元素乘积是: " + t1.product)                //720
    //5. 通过max()方法获取t1集合中所有元素的最大值,  并打印结果.
    println("所有的元素最大值是: " + t1.max)                 //6
    //6. 通过min()方法获取t1集合中所有元素的最小值,  并打印结果.
    println("所有的元素最小值是: " + t1.min)                 //1
  }
}
