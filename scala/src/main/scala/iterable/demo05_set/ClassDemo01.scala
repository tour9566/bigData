package iterable.demo05_set

import scala.collection.{SortedSet, mutable}

//案例: 演示Set集合.
object ClassDemo01 {
  def main(args: Array[String]): Unit = {
    //1. 创建SortedSet集合, 存储元素1, 4, 3, 2, 5, 然后打印该集合.
    val s1 = SortedSet(1, 4, 3, 2, 5, 3, 2)   //TreeSet: 唯一, 排序(默认是升序)
    println(s"s1: ${s1}")   //1, 2, 3, 4, 5
    //2. 创建HashSet集合, 存储元素1, 4, 3, 2, 5, 然后打印该集合.
    val s2 = mutable.HashSet(1, 4, 3, 2, 5, 3, 2)   //唯一, 无序.
    println(s"s2: ${s2}")
    //3. 创建LinkedHashSet集合, , 存储元素1, 4, 3, 2, 5, 然后打印该集合.
    val s3 = mutable.LinkedHashSet(1, 4, 3, 2, 5, 3, 2) //唯一, 有序.
    println(s"s3: ${s3}")   //1, 4, 3, 2, 5
  }
}
