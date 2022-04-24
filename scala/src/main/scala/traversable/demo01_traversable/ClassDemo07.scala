package traversable.demo01_traversable

//案例: 判断元素是否合法
object ClassDemo07 {
  def main(args: Array[String]): Unit = {
    //1. 定义Traversable集合t1, 包含1到6这六个数字.
    val t1 = Traversable(1, 2, 3, 4, 5, 6)
    //2. 通过forall()方法实现, 判断t1中的元素是否都是偶数.
    println(t1.forall(_ % 2 == 0))      //要求所有的元素都是偶数.

    //方案: 通过filter()方法来实现, 但是在这里不推荐, 因为该方法会返回一个集合对象.
    //        过滤出所有的奇数         只要奇数的个数为0, 说明集合中所有的元素都是偶数.
    println(t1.filter(_ % 2 != 0).size == 0)
    println("-" * 15)

    //3. 通过exists()方法实现, 判断t1中是否有偶数.
    println(t1.exists(_ % 2 == 0))     //要求只要有一个元素是偶数即可.
  }
}
