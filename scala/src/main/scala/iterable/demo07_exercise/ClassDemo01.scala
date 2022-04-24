package iterable.demo07_exercise

import scala.collection.mutable
import scala.io.StdIn

//案例: 统计字符个数.
object ClassDemo01 {
  def main(args: Array[String]): Unit = {
    //1. 提示用户录入字符串, 并接收.
    println("请录入一个字符串: ")
    val str = StdIn.readLine()
    //2. 定义Map集合, 用来存储字符及其出现的次数. 键:字符, 值: 字符出现的次数.
    val map = mutable.Map[Char, Int]()
    //3. 将字符串转成字符数组.
    val chs = str.toCharArray
    //4. 遍历字符数组, 获取到每一个字符.
    for (k <- chs) {
      //k: 就是字符串中的每一个字符.
      //5. 如果字符是第一次出现, 就将其次数记录为1, 如果字符是重复出现, 就将其次数+1, 然后重新存储.
      //5.1 如果字符是第一次出现, 就将其次数记录为1
      if (!map.contains(k)) {
        map += (k -> 1)
      } else {
        //5.2 如果字符是重复出现, 就将其次数+1, 然后重新存储
        //'a' -> 2, 'b' -> 1
        map += (k -> (map.getOrElse(k, 1) + 1))
      }
    }
    //6. 遍历集合, 查看结果.
    map.foreach(println(_))
  }
}
