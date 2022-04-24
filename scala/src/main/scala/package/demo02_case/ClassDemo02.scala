package `package`.demo02_case

//案例: 演示样例类的默认方法.
object ClassDemo02 {
  //1. 定义样例类Person.
  case class Person(var name:String, var age:Int) {}

  //2. 定义main方法, 作为程序的主入口.
  def main(args: Array[String]): Unit = {
    //3. 创建Person类型的对象, 测试默认的一些方法.
    //apply(): 让我们快速的创建对象, 省去new关键字.
    val p1 = Person("张三", 23)
    //toString(): 输出语句打印对象, 默认调用了对象的toString()方法, 重写它以后, 可以方便我们快速输出对象的各个属性值.
    println(p1)
    //equals(): 可以让我们通过==的形式, 比较两个对象的各个属性值是否相同.
    val p2 = Person("张三", 23)
    println(p1 == p2)

    //hashCode(): 可以获取对象的哈希值.
    //记忆: 同一个对象哈希值肯定相同, 不同对象哈希值一般不同.
    println(p1.hashCode())
    println(p2.hashCode())
    println("-" * 15)
    //举例: 内容不同, 但是哈希值相同.
    println("重地1".hashCode)
    println("通话1".hashCode)

    //copy(): 可以让我们基于某一个对象, 快速的构建一个和它类似的对象.
    val p3 = p2.copy(age = 30)
    println(p3)
  }

}
