package `package`.demo02_case

//案例: 样例类入门
object ClassDemo01 {
  //1. 定义样例类Person.
  case class Person(name:String = "张三", var age:Int = 23) {}    //val是可以省略不写的.

  //2. 定义main方法,作为程序的主入口.
  def main(args: Array[String]): Unit = {
    //3. 创建Person类型的对象.
    val p = new Person
    //4. 打印Person类中的属性值.
    println(s"修改前: ${p.name}, ${p.age}")

    //5. 尝试修改Person类中的属性值.
    //p.name = "李四"       //这样写会报错, 因为我们在定义成员变量时没有用val或者var来修饰, 系统会默认给我们加上val来修饰.
    p.age = 30

    //6. 打印修改后的结果.
    println(s"修改后: ${p.name}, ${p.age}")
  }
}
