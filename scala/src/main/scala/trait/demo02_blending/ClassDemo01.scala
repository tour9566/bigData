package `trait`.demo02_blending

//案例: 演示动态混入
object ClassDemo01 {
  //1. 创建Logger特质,  添加log(msg:String)方法
  trait Logger {
    def log(msg:String) = println(msg)
  }

  //2. 创建一个User类, 该类和Logger特质之间无任何关系.
  class User

  //3. 在main方法中测试, 通过对象混入技术让User类的对象具有Logger特质的log()方法.
  def main(args: Array[String]): Unit = {
    //通过对象混入技术让User类的对象具有Logger特质的log()方法.
    val u1 = new User with Logger
    u1.log("演示动态混入: 所谓的动态混入指的就是, 让某个类的对象临时可以访问某个特质中的成员")

    //这样写, 代码会报错.
    /*val u2 = new User
    u2.log("")*/
  }
}
