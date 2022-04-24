package iterable.demo02_seq

//案例: 判断集合是否包含指定数据
object ClassDemo04 {
  def main(args: Array[String]): Unit = {
    //1. 定义Seq集合s1, 存储1到10这十个数据.
    val s1 = (1 to 10).toSeq
    //2. 判断s1集合是否以子序列(1, 2)开头, 并打印结果.
    println(s1.startsWith(Seq(1, 2)))             //true
    //3. 判断s1集合是否以子序列(1, 3)开头, 并打印结果.
    println(s1.startsWith(Seq(1, 3)))             //false
    //4. 判断s1集合是否以子序列(9, 10)结尾, 并打印结果.
    println(s1.endsWith(Seq(9, 10)))              //true
    //5. 判断s1集合是否以子序列(8, 9)结尾, 并打印结果.
    println(s1.endsWith(Seq(8, 9)))             //false
    println("-" * 15)

    //6. 判断s1集合是否包含元素3, 并打印结果.
    println(s1.contains(3))                     //true
    //7. 判断s1集合是否包含子序列Seq(1, 2), 并打印结果.
    println(s1.containsSlice(Seq(1, 2)))        //true
    //8. 判断s1集合是否包含子序列Seq(1, 3), 并打印结果.
    println(s1.containsSlice(Seq(1, 3)))        //false
  }
}
