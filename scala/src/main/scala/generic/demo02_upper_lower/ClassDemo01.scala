package generic.demo02_upper_lower

//案例: 演示泛型的上下界之  上界.
object ClassDemo01 {
  //1. 定义一个Person类
  class Person
  //2. 定义一个Student类，继承Person类
  class Student extends Person
  //3. 定义一个泛型方法demo()，该方法接收一个Array参数.
  //4. 限定demo方法的Array元素类型只能是Person或者Person的子类
  def demo[T <: Person](arr: Array[T]) = println(arr)

  def main(args: Array[String]): Unit = {
    //5. 测试调用demo()方法，传入不同元素类型的Array
    demo(Array(new Person(), new Person()))
    demo(Array(new Student(), new Student()))

    //demo(Array("a", "b", "c"))    这样代码会报错.
  }
}
