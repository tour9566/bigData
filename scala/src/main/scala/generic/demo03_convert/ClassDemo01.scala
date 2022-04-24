package generic.demo03_convert

//案例: 演示非变, 协变, 逆变.
object ClassDemo01 {

  //1. 定义一个Super类、以及一个Sub类继承自Super类
  class Super //父类
  class Sub extends Super //子类

  //2. 使用协变、逆变、非变分别定义三个泛型类
  //非变
  class Temp1[T]

  //协变
  class Temp2[+T]

  //逆变
  class Temp3[-T]

  def main(args: Array[String]): Unit = {
    //3. 分别创建泛型类对象来演示协变、逆变、非变
    //3.1 测试非变.
    val t1:Temp1[Sub] = new Temp1[Sub]
    //val t2:Temp1[Super] = t1        编译报错, 因为是非变,  Super和Sub有父子类关系, 但是Temp1[Super]和Temp1[Sub]无任何关系.

    //3.2 测试协变.
    val t3:Temp2[Sub] = new Temp2[Sub]
    val t4:Temp2[Super] = t3        //协变, Super和Sub有父子类关系, Temp2[Super]和Temp2[Sub]之间也有父子类关系

    //3.3 测试逆变.
    /*val t5:Temp3[Sub]  = new Temp3[Sub]
    val t6:Temp3[Super] = t5 */     //编译报错, 逆变是. Super和Sub有父子类关系, Temp3[Super]和Temp3[Sub]之间有  子父类 关系

    val t7:Temp3[Super] = new Temp3[Super]
    val t8:Temp3[Sub] = t7        //逆变, 不会报错.
  }
}
