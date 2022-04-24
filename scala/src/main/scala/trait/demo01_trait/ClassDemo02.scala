package `trait`.demo01_trait

//案例: 演示类继承多个trait
object ClassDemo02 {

  //1. 创建一个MessageSender特质，添加`send(msg:String)`方法
  trait MessageSender {
    def send(msg: String) //抽象方法, 表示发送消息.
  }

  //2. 创建一个MessageReceiver特质，添加`receive()`方法
  trait MessageReceiver {
    def receive() //抽象方法, 表示接收消息.
  }

  //3. 创建一个MessageWorker类, 继承这两个特质, 重写上述的两个方法
  class MessageWorker extends MessageSender with MessageReceiver {
    override def send(msg: String): Unit = println(s"发送消息: ${msg}")

    override def receive(): Unit = println("消息已收到, 我很好, 谢谢!")
  }

  //4. 在main中测试，分别调用send方法、receive方法
  def main(args: Array[String]): Unit = {
    //5. 创建MessageWorker类的对象.
    val mw = new MessageWorker
    //6. 调用send方法、receive方法
    mw.send("Hello, 你好呀!")
    mw.receive()
  }
}
