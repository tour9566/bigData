package oop.demo05_constructor

//案例: 演示辅助构造器.
object ClassDemo02 {

  //1. 定义Customer类, 在主构造器中指定: 姓名, 地址.
  class Customer(val name: String, val address: String) {   //"张三", "北京"
    //2. 定义辅助构造器, 接收一个数组类型的参数.
    def this(arr:Array[String]) { // arr = Array("张三", "北京")
      //细节: 辅助构造器的第一行代码必须访问主构造器或者其他的辅助构造器.
      this(arr(0), arr(1))    //"张三", "北京"
    }
  }

  //3. 定义main方法, 作为程序的主入口.
  def main(args: Array[String]): Unit = {
    //4. 通过辅助构造器, 创建Customer类的对象.
    val c = new Customer(Array("张三", "北京"))
    //val c2 = new Customer("李四", "上海")     //通过主构造器创建对象.
    //5. 打印属性值.
    println(c.name, c.address)
  }
}
