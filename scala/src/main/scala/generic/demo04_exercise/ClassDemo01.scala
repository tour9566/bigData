package generic.demo04_exercise

import java.io.{BufferedWriter, FileWriter}

import scala.io.Source

//案例: 列表去重排序, 并写入文件.
object ClassDemo01 {
  def main(args: Array[String]): Unit = {
    //1. 定义数据源对象, 关联数据源文件.
    val source = Source.fromFile("./data/1.txt")
    //2. 从指定的文件中读取所有的数据.
    //所有的数据 -> 按照空白字符切割, Array("11", "6", "5") -> List[String]
    val list1:List[String] = source.mkString.split("\\s+").toList
    //3. 把List[String] -> List[Int]
    val list2: List[Int] = list1.map(_.toInt)     //List(11, 6, 5, 5)
    //4. 把List[Int] -> Set[Int], 对列表元素去重.
    val set:Set[Int] = list2.toSet                //Set(11, 6, 5)
    //5. Set[Int] -> List[Int], 然后升序排列.
    //此时, list3列表中记录的就是我们想要的数据.
    val list3:List[Int] = set.toList.sorted       //List(1, 2, 3, 5, 6, 9, 11, 22)
    //println(list3)
    //6. 把所有的数据写入到指定的目的地文件中.
    //6.1 创建字符缓冲流, 用来写入数据到指定的目的地文件中.
    val bw = new BufferedWriter(new FileWriter("./data/2.txt"))
    //6.2 遍历list3列表, 获取每一个数字
    for(i <- list3) {
      //i就表示list3列表中的每一个数字, 例如: 1, 2, 3, 5, 6, 9, 11, 22
      //6.3 将获取到的数字转成字符串以后, 再写入.
      bw.write(i.toString)
      //6.4 记得加换行.
      bw.newLine()
    }
    //7. 释放资源.
    bw.close()
    source.close()
  }
}
