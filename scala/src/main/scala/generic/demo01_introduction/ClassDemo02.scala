package generic.demo01_introduction

//案例: 泛型-演示泛型类的使用.
//泛型类: 在创建对象的时候, 明确具体的数据类型.
object ClassDemo02 {
  //1. 定义一个Pair泛型类, 该类包含两个字段，且两个字段的类型不固定.
  class Pair[T](var a:T, var b:T)

  def main(args: Array[String]): Unit = {
    //2. 创建不同类型的Pair泛型类对象，并打印.
    //创建对象
    val p1 = new Pair[Int](10, 20)
    //打印结果.
    println(p1.a, p1.b)
    println("---------------------")

    val p2 = new Pair[String]("abc", "xyz")
    println(p2.a, p2.b)
  }
}
