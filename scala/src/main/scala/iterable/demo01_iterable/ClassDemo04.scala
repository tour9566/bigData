package iterable.demo01_iterable

import scala.collection.immutable.{HashSet, TreeSet}

//案例: 检查两个Iterable是否包含相同的元素.
//sameElements(): 要求集合的元素及迭代顺序都一致, 才会返回true, 否则返回false.
object ClassDemo04 {
  def main(args: Array[String]): Unit = {
    //1. 定义Iterable集合list1, 包含"A", "B", "C"这三个元素.
    val list1 = Iterable("A", "B", "C")
    //2. 通过sameElements()方法, 判断list1和Iterable("A", "B", "C")集合是否相同.
    println(list1.sameElements(Iterable("A", "B", "C")))      //true
    //3. 通过sameElements()方法, 判断list1和Iterable("A", "C", "B")集合是否相同.
    println(list1.sameElements(Iterable("A", "C", "B")))     //false
    //4. 定义Iterable集合list2, 包含"A", "B", "C", "D"这四个元素.
    val list2 = Iterable("A", "B", "C", "D")
    //5. 通过sameElements()方法, 判断list1和list2是否相同.
    println(list1.sameElements(list2))                      //false

    //6. 扩展需求
    //6.1 定义HashSet集合, 存储元素1, 2
    val hs = HashSet(1, 2)
    //6.2 定义TreeSet集合, 存储元素2, 1
    val ts = TreeSet(2, 1)        //TreeSet集合会默认对集合元素进行升序排列.
    //6.3 通过sameElements()方法判断, 这两个集合是否一致.
    println(hs.sameElements(ts))      //true
  }
}
