package `package`.demo01_package

//案例: 演示包的引入
object ClassDemo05 {
  def main(args: Array[String]): Unit = {
    //1. 创建测试类, 并在main方法中测试上述的5点注意事项.
    //2. 需求1: 导入java.util.HashSet类.
    /*import java.util.HashSet
    val hs = new HashSet()
    println(hs.getClass)*/

    //3. 需求2: 导入java.util包下所有的内容.
    /*import java.util._
    val list = new ArrayList()
    val hs = new HashSet()
    println(list.getClass(), hs.getClass())*/

    //4. 需求3: 只导入java.util包下的ArrayList类和HashSet类
    /*import java.util.{ArrayList, HashSet}
    val list = new ArrayList()
    val hs = new HashSet()
    //val hm = new HashMap()      这样写会报错, 因为没有导包.
    println(list.getClass(), hs.getClass())*/

    //5. 需求4: 通过重命名的方式, 解决多个包中类名重复的问题
    /*import java.util.{HashSet=>JavaHashSet}
    import scala.collection.mutable.HashSet
    val hs = new HashSet()
    println(hs.getClass)

    val jhs = new JavaHashSet()
    println(jhs.getClass)*/

    //6. 需求5: 导入时, 隐藏某些不需要用到的类, 即: 导入java.util包下除了HasSet和TreeSet之外所有的类.
    import java.util.{HashSet=>_, TreeSet=>_, _}
    import scala.collection.mutable.HashSet
    val hs = new HashSet()
    println(hs.getClass)
  }
}
