package generic.demo01_introduction

//案例: 演示泛型方法.
object ClassDemo01 {
  //需求: 定义方法getMiddleElement(), 用来获取任意类型数组的中间元素.
  //方式一: 不采用泛型, 即: 普通的方法.
  //def getMiddleElement(arr:Array[Any]) = arr(arr.length / 2)

  //方式二: 采用自定义的泛型方法来实现.
  //T:  type单词的缩写, 类型.
  def getMiddleElement[T](arr:Array[T]) = arr(arr.length / 2)

  def main(args: Array[String]): Unit = {
    //测试: getMiddleElement()方法
    println(getMiddleElement(Array(1, 2, 3, 4, 5)))

    println(getMiddleElement(Array("a", "b", "c")))
  }
}
