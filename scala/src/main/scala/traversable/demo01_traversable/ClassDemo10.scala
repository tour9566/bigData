package traversable.demo01_traversable

import scala.util.Random

//案例: 演示填充元素.
object ClassDemo10 {
  def main(args: Array[String]): Unit = {
    //1. 通过fill()方法, 生成一个Traversable集合, 该集合包含5个元素, 值都是"传智播客".
    //5表示集合中有5个元素, "传智播客"表示每个元素的具体值.
    println(Traversable.fill(5)("传智播客"))
    println("-" * 15)
    //2. 通过fill()方法, 生成一个Traversable集合, 该集合包含3个随机数.
    println(Traversable.fill(3)(Random.nextInt(100)))
    println("-" * 15)
    //3. 通过fill()方法, 生成一个Traversable集合, 格式如下:
    //List(List(传智播客, 传智播客), List(传智播客, 传智播客), List(传智播客, 传智播客), List(传智播客, 传智播客), List(传智播客, 传智播客))
    //5表示生成的集合中有5个元素对象(每个元素对象都是一个集合对象),  2的意思是: 每个集合对象的长度为2
    println(Traversable.fill(5, 2)("传智播客"))
    println("-" * 15)
    //4. 通过iterate()方法, 生成一个Traversable集合, 该集合包含5个元素, 分别为:1, 10, 100, 1000, 10000.
    //1表示初始化值, 5表示最终要获取的元素的个数.   _ * 10  函数对象, 表示具体的规则.
    println(Traversable.iterate(1, 5)(_ * 10))
    println("-" * 15)
    //5. 通过range()方法, 获取从数字1开始, 截止数字21之间, 间隔为5的所有数据.
    //1表示从1开始, 21表示到数字21结束(包左不包右), 间隔为5
    println(Traversable.range(1, 21, 5))      //1, 6, 11, 15

    //如果没有传入间隔值, 默认间隔为1
    println(Traversable.range(1, 21))         //1, 2, 3, 4...20
  }
}
