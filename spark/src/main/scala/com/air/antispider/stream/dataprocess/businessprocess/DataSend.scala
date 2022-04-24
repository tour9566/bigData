package com.air.antispider.stream.dataprocess.businessprocess

import java.util

import com.air.antispider.stream.common.bean.ProcessedData
import com.air.antispider.stream.common.util.jedis.PropertiesUtil
import com.air.antispider.stream.dataprocess.constants.BehaviorTypeEnum
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}
import org.apache.spark.rdd.RDD

//用于实现数据发送功能
//查询的数据推送到查询的topic
//预定的数据推送到预定的topic
object DataSend {
  //将查询的数据推送到查询的topic
  def sendQueryDataToKafka(DataProcess: RDD[ProcessedData]): Unit = {
    //过滤出纯查询的数据（过滤掉预定的数据）  使用“#CS#”进行拼接
  val queryDatas = DataProcess.filter(message=>message.requestType.behaviorType==BehaviorTypeEnum.Query).map(message=>message.toKafkaString())

    //将数据推送到kafka
    //1在配置文件中读取查询类的Topic到程序中
   val queryTopic= PropertiesUtil.getStringByKey("target.query.topic","kafkaConfig.properties")


    //实例kafka参数
    val props=new util.HashMap[String,Object]()
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,PropertiesUtil.getStringByKey("default.brokers","kafkaConfig.properties"))
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,PropertiesUtil.getStringByKey("default.key_serializer_class_config","kafkaConfig.properties"))
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,PropertiesUtil.getStringByKey("default.value_serializer_class_config","kafkaConfig.properties"))
    props.put(ProducerConfig.BATCH_SIZE_CONFIG,PropertiesUtil.getStringByKey("default.batch_size_config","kafkaConfig.properties"))
    props.put(ProducerConfig.LINGER_MS_CONFIG,PropertiesUtil.getStringByKey("default.linger_ms_config","kafkaConfig.properties"))


    //遍历数据的分区
    queryDatas.foreachPartition(partition=>{
      //2创建kafka生产者
      val kafkaProducer=new KafkaProducer[String,String](props)
      //遍历partition 内的数据
      partition.foreach(message=>{
        //3数据的载体
        val record=new ProducerRecord[String,String](queryTopic,message)
        //4数据的发送
        kafkaProducer.send(record)

      })
      //5关闭生成者
      kafkaProducer.close()
    })

  }


  //将预定的数据推送到预定的topic
  def sendBookDataToKafka(DataProcess: RDD[ProcessedData]): Unit = {
    //过滤出纯预定的数据（过滤掉查询的数据）  使用“#CS#”进行拼接
    val bookDatas = DataProcess.filter(message=>message.requestType.behaviorType==BehaviorTypeEnum.Book).map(message=>message.toKafkaString())

    //将数据推送到kafka
    //1在配置文件中读取预定类的Topic到程序中
    val bookTopic= PropertiesUtil.getStringByKey("target.book.topic","kafkaConfig.properties")


    //实例kafka参数
    val props=new util.HashMap[String,Object]()
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,PropertiesUtil.getStringByKey("default.brokers","kafkaConfig.properties"))
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,PropertiesUtil.getStringByKey("default.key_serializer_class_config","kafkaConfig.properties"))
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,PropertiesUtil.getStringByKey("default.value_serializer_class_config","kafkaConfig.properties"))
    props.put(ProducerConfig.BATCH_SIZE_CONFIG,PropertiesUtil.getStringByKey("default.batch_size_config","kafkaConfig.properties"))
    props.put(ProducerConfig.LINGER_MS_CONFIG,PropertiesUtil.getStringByKey("default.linger_ms_config","kafkaConfig.properties"))


    //遍历数据的分区
    bookDatas.foreachPartition(partition=>{
      //2创建kafka生产者
      val kafkaProducer=new KafkaProducer[String,String](props)
      //遍历partition 内的数据
      partition.foreach(message=>{
        //3数据的载体
      val record=new ProducerRecord[String,String](bookTopic,message)
      //4数据的发送
      kafkaProducer.send(record)

    })
    //5关闭生成者
    kafkaProducer.close()
    })

  }
}
