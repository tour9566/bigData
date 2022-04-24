package iterable.demo02_seq

//案例: 获取指定元素的索引值
object ClassDemo03 {
  def main(args: Array[String]): Unit = {
    //1. 定义Seq集合, 存储数据: 1, 2, 4, 6, 4, 3, 2.
    val s1 = Seq(1, 2, 4, 6, 4, 3, 2)
    //2. 获取元素2在集合中第一次出现的索引, 并打印.
    println(s1.indexOf(2))                                                //1
    //3. 获取元素2在集合中最后一次出现的索引, 并打印.
    println(s1.lastIndexOf(2))                                            //6
    println("-" * 15)

    //4. 获取集合中, 小于5的第一个偶数的索引, 并打印.
    println(s1.indexWhere(x => x < 5 && x % 2 == 0))                      //1
    //5. 从索引2位置开始查找集合中, 小于5的第一个偶数的索引, 并打印.
    //第一个参数的意思: 查找规则,   第二个参数的意思: 从哪里(索引)开始查找.
    println(s1.indexWhere(x => x < 5 && x % 2 == 0, 2))                   //2
    //6. 获取集合中, 小于5的最后一个偶数的索引, 并打印.
    println(s1.lastIndexWhere(x => x < 5 && x % 2 == 0))                  //6
    println("-" * 15)

    //7. 获取子序列Seq(1, 2)在s1集合中, 第一次出现的索引, 并打印.
    println(s1.indexOfSlice(Seq(1, 2)))                                   //0
    //8. 从索引3开始查找子序列Seq(1, 2)在s1集合中, 第一次出现的索引, 并打印.
    println(s1.indexOfSlice(Seq(1, 2), 3))                               //-1

  }
}
