package traversable.demo02_exercise

import scala.util.Random


//案例: 随机学生序列
object ClassDemo01 {

  //1. 创建Student样例类, 属性为: 姓名, 年龄, 用来记录学生的信息.
  case class Student(name: String, age: Int)

  def main(args: Array[String]): Unit = {
    //2. 定义列表, 记录学生的姓名信息, 值为: "张三", "李四", "王五", "赵六", "田七".
    val names: List[String] = List("张三", "李四", "王五", "赵六", "田七")
    //3. 创建随机数对象r, 用来实现获取一些随机值的操作.
    val r:Random = new Random()
    //4. 创建Traversable集合, 包含5个随机的学生信息.
    //val t1:Traversable[Student] = Traversable.fill(5)(new Student("随机的姓名", "随机的年龄"))
    val t1:Traversable[Student] = Traversable.fill(5)(new Student(names(r.nextInt(names.size)), r.nextInt(10) + 20))     //年龄的范围是: 20 - 30, 包左不包右.
    //5. 将Traversable集合转换成List列表.
    val t2:List[Student] = t1.toList
    //6. 通过列表的sortWith()方法, 按照学生的年龄降序排列.
    //思路一: 通过sortBy()方法实现.
    //val sortList:List[Student] = t2.sortBy(_.age).reverse

    //思路二: 通过sortWith()方法实现.
    val sortList:List[Student] = t2.sortWith(_.age > _.age)

    //7. 打印结果.
    println(s"sortList: ${sortList}")
  }
}
