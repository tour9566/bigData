package generic.demo02_upper_lower

//案例: 演示泛型的上下界之 下界.
//细节: 如果你在设定泛型的时候, 同时涉及到了上界, 下界, 那么: 下界在前, 上界在后.
object ClassDemo02 {
  //1. 定义一个Person类
  class Person
  //2. 定义一个Policeman类，继承Person类
  class Policeman extends Person
  //3. 定义一个Superman类，继承Policeman类
  class Superman extends Policeman
  //4. 定义一个demo泛型方法，该方法接收一个Array参数，
  //5. 限定demo方法的Array元素类型只能是Person、Policeman
  def demo[T >: Policeman](arr: Array[T]) = println(arr)

  //需求: 限定这里只能传入Policeman类型
  //        下界            上界
  //def demo[T >: Policeman <: Policeman](arr: Array[T]) = println(arr)


  def main(args: Array[String]): Unit = {
    //6. 测试调用demo，传入不同元素类型的Array
    //传入Person类型的数组.
    demo(Array(new Person()))
    //传入Policeman类型的数组.
    demo(Array(new Policeman(), new Policeman()))

    //传入Superman类型的数组.
    //demo(Array(new Superman()))

    //传入String类型的数组.
    //demo(Array("a", "b", "c"))
  }
}
