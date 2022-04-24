package `package`.demo02_case

//案例: 演示样例对象之当做枚举使用.
object ClassDemo03 {
  //1. 定义特质Sex, 表示性别.
  trait Sex
  //2. 定义样例对象Male, 表示男, 继承Sex特质.
  case object Male extends Sex
  //3. 定义样例对象Female, 表示女, 继承Sex特质.
  case object Female extends Sex

  //4. 定义样例类Person, 属性: 姓名, 性别.
  case class Person(var name:String, var sex:Sex) {}

  //5. 定义main方法, 作为程序的主入口.
  def main(args: Array[String]): Unit = {
    //6. 创建样例类Person的对象, 并打印属性值.
    val p = Person("张三", Female)
    println(p.name, p.sex)
  }

}
