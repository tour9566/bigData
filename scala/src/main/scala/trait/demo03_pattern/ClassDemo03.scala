package `trait`.demo03_pattern

//案例: 演示职责链模式(也叫: 责任链模式, 调用链模式)
/*
  执行顺序:
    1. 按照从右往左的顺序, 依次执行特质中的方法.
    2. 当子特质中的方法执行完毕后, 最后执行父特质中的方法.
 */
object ClassDemo03 {

  //1. 定义一个Handler特质, 添加具体的handle(data:String)方法，表示处理数据(具体的支付逻辑)
  trait Handler {
    def handle(data: String) = { //data记录的是: 用户发起的具体支付数据.
      println("具体的处理数据...  4")
      println(data + "  5")
    }
  }

  //2. 定义一个DataValidHandler特质，继承Handler特质.
  trait DataValidHandler extends Handler {
    //重写handle()方法，打印"验证数据", 然后调用父特质的handle()方法
    override def handle(data: String): Unit = {
      //打印"验证数据"
      println("验证数据   3")
      //核心: 调用父特质的handle()方法
      super.handle(data)
    }
  }

  //3. 定义一个SignatureValidHandler特质，继承Handler特质.
  trait SignatureValidHandler extends Handler {
    //重写handle()方法, 打印"检查签名", 然后调用父特质的handle()方法
    override def handle(data: String): Unit = {
      //打印"检查签名"
      println("检查签名    2")
      //核心: 调用父特质的handle()方法
      super.handle(data)
    }
  }

  //4. 创建一个Payment类, 继承DataValidHandler特质和SignatureValidHandler特质
  class Payment extends DataValidHandler with SignatureValidHandler { //叠加特质
    //定义pay(data:String)方法, 打印"用户发起支付请求", 然后调用父特质的handle()方法
    def pay(data:String) = {    //data:  "张三给凤姐转账1000元"
      //打印"用户发起支付请求"
      println("用户发起支付请求   1")

      //调用父特质的handle()方法
      super.handle(data)          //这就构成了一个: 调用链.
    }
  }

  //5. 添加main方法, 创建Payment对象实例, 然后调用pay()方法.
  def main(args: Array[String]): Unit = {
    //创建Payment对象实例, 然后调用pay()方法.
    val p = new Payment
    p.pay("张三给凤姐转账1000元")
  }
}
