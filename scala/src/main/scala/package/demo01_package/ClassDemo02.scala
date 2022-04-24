//案例: 演示包的作用域.

/*
  包的作用域(访问规则)
    1. 下层可以直接访问上层的内容.
    2. 如果上下层有相同的类时, 我们会采用就近原则来访问.
       如果明确需求访问上层的类, 可以通过包名+类名的形式实现.
    3. 上层访问下层内容时, 要先导包.
 */

//1. 创建com.itheima包, 并在其中定义Person类, Teacher类, 及子包scala.
package `package`.demo01_package

//2. 在com.itheima.scala包中定义Person类, Student类.
//    class Person      //路径: com.itheima.Person
//  class Student     //路径: com.itheima.Teacher

  //测试类
  object ClassDemo02 {
    def main(args: Array[String]): Unit = {
      //3.4 上层访问下层内容时, 要先导包.
      //import com.itheima.scala.Student
//      import scala.Student
//      val s = new Student
//      println(s)
    }
  }

  //2. 在com.itheima.scala包中定义Person类, Student类.
//    class Person    //路径: com.itheima.scala.Person
//    class Student   //路径: com.itheima.scala.Student

    //3. 在测试类中测试.
   /* object ClassDemo02 {
      def main(args: Array[String]): Unit = {
        //3.1 测试 下层可以直接访问上层的内容.
        val t = new Teacher
        println(t)

        //3.2 如果上下层有相同的类时, 我们会采用就近原则来访问.
        val p1 = new Person
        println(p1)

        //3.3 如果明确需求访问上层的类, 可以通过包名+类名的形式实现.
        val p2 = new com.itheima.Person
        println(p2)
      }
    }*/


