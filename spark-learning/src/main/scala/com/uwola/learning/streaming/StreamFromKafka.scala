package com.uwola.learning.streaming

import java.text.SimpleDateFormat
import java.util.Date

import com.alibaba.fastjson.JSON
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{DataFrame, Row, SaveMode, SparkSession}
import org.apache.spark.streaming._
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.kafka.KafkaUtils

/**
  * 参考：https://blog.csdn.net/jianghuxiaojin/article/details/51452682
  * Created by uwoer
  */
object StreamFromKafka {

  case class Order(order_id:String,
                    user_id:String,
                    eval_set:String,
                    order_number:String,
                    order_dow:String,
                    order_hour_of_day:String,
                    days_since_prior_order:String)
    //  temp  test  5 20180612
    def main(args: Array[String]): Unit = {
      if (args.length < 3) {
        System.err.print("Usage: Collect log from Kafka <groupid> <topic> <Execution_time>")
        System.exit(1)
      }

//    val Array(group_id, topic, exectime, dt,path) = args
      val Array(group_id, topic, exectime,dt) = args

//      val dt = getNowDate();

//      zookeeper IP:Port
      val zkHostIp = Array("130","131","132").map("192.168.40."+_)
//      val zkHostIp = Array("master","slave1","slave2")
      val ZK_QUORUM = zkHostIp.map(_+":2181").mkString(",")

//      val dt = getNowDate()
      val numThreads = 1
      /*
       * 第一步：配置SparkConf：
       * 1，至少2个线程：因为Spark Streaming应用程序在运行的时候，至少有一个
       * 线程用于不断的循环接收数据，并且至少有一个线程用于处理接受的数据（否则的话无法
       * 有线程用于处理数据，随着时间的推移，内存和磁盘都会不堪重负）；
       * 2，对于集群而言，每个Executor一般肯定不止一个Thread，那对于处理Spark Streaming的
       * 应用程序而言，每个Executor一般分配多少Core比较合适？根据我们过去的经验，5个左右的
       * Core是最佳的（一个段子分配为奇数个Core表现最佳，例如3个、5个、7个Core等）；
       */
      val conf = new SparkConf()
      //本地调试时加上
      conf.setAppName("StreamFromKafka")
      //本地调试时一定要加上，或则程序无法正常运行
      conf.setMaster("local[3]")
      val ssc = new StreamingContext(conf,Seconds(exectime.toInt))
      val topicSet = topic.split(",").toSet
      val topicMap = topicSet.map((_,numThreads.toInt)).toMap

      /*
       * 第三步：创建Spark Streaming输入数据来源input Stream：
       * 1，数据输入来源可以基于File、HDFS、Flume、Kafka、Socket等
       * 2, 在这里我们指定数据来源于网络Socket端口，Spark Streaming连接上该端口并在运行的时候一直监听该端口
       *    的数据（当然该端口服务首先必须存在）,并且在后续会根据业务需要不断的有数据产生(当然对于Spark Streaming
       *    应用程序的运行而言，有无数据其处理流程都是一样的)；
       * 3,如果经常在每间隔5秒钟没有数据的话不断的启动空的Job其实是会造成调度资源的浪费，因为并没有数据需要发生计算，所以
       *   实例的企业级生成环境的代码在具体提交Job前会判断是否有数据，如果没有的话就不再提交Job；
       */

      //      通过Receiver接收kafka数据
      val mesR = KafkaUtils.createStream(ssc,ZK_QUORUM,group_id,topicMap).map(_._2)
//      mesR.print()
      def rdd2DataFrame(rdd:RDD[String]): DataFrame ={
        val spark = SparkSession
          .builder()
          .appName("Streaming Form Kafka Static")
          // 打开hive动态分区和非严格模式
          .config("hive.exec.dynamic.partition", "true")
          .config("hive.exec.dynamic.partition.mode", "nonstrict")
          .enableHiveSupport()
          .getOrCreate()
        import spark.implicits._
        rdd.map { x =>
//          println(x)
          val mess = JSON.parseObject(x, classOf[Orders])
          Order(mess.order_id,
            mess.user_id,
            mess.eval_set,
            mess.order_number,
            mess.order_dow,
            mess.order_hour_of_day,
            mess.days_since_prior_order)
        }.toDF()
      }

      //uv 10s内的统计
      ssc.checkpoint("checkpoint")
      uv(mesR)

//ctrl Q  查看变量类型
      /*val log = mesR.foreachRDD { rdd =>
        val df=rdd2DataFrame(rdd)
        df.withColumn("dt",lit(dt))
          .write.mode(SaveMode.Append)
          .insertInto("logsdb.order_partition")
      }*/

//      log.filter(_._1=="right").map(_._2)
//        .foreachRDD{ rdd =>
//          if(dynamic.toInt==1) rddSave(rdd,Schema.NewKeyWordSchema,"badou.fact_log_static")
//          else rddSaveTable(rdd,Schema.NewKeyWordSchema,"logsdb.fact_log")
//      }
//      log.filter(_._1=="wrong").map(_._2)
//        .foreachRDD{rdd=>
//          val dt = getNowDate()
//          val rdd1 = rdd.map(r=>Row(r.getString(0),r.getString(1),dt))
//          if(dynamic.toInt==1) rddSave(rdd1,Schema.WrongNewKeyWordSchema,"badou.error_fact_log_static")
//          else rddSaveTable(rdd1,Schema.WrongNewKeyWordSchema,"logsdb.error_fact_log")
//      }

      ssc.start()
      ssc.awaitTermination()
    }

  /**
    *以表的形式保存
    */
  def rddSave(rdd:RDD[Row],schema:StructType,tableName:String){
    val records = rdd.coalesce(1)
    val spark = SparkSession
      .builder()
      .appName("Streaming Form Kafka Static")
      .enableHiveSupport()
      .getOrCreate()
    val df = spark.createDataFrame(records,schema)
    df.write.mode(SaveMode.Append).saveAsTable(tableName)
  }

  /**
    * 插入表
    */
  def rddSaveTable(rdd:RDD[Row],schema:StructType,tableName:String){
    val records = rdd.coalesce(1)
    // 打开hive动态分区和非严格模式
    val spark = SparkSession.builder()
//      .config("spark.sql.warehouse.dir","hdfs:///user/hive/warehouse/badou.db")
      .config("hive.exec.dynamic.partition", "true")
      .config("hive.exec.dynamic.partition.mode", "nonstrict")
      .enableHiveSupport()
      .getOrCreate()
//    spark.sql("set hive.exec.dynamic.partition=true;")
//    spark.sql("set hive.exec.dynamic.partition.mode=nonstrict;")
    val df = spark.createDataFrame(records,schema)
    df.write.mode(SaveMode.Append).insertInto(tableName)
  }

  /**
    * uv 统计
    */
  def uv(dStream: DStream[String]): Unit ={
    //uv 30s内的统计
    val log = dStream.map(x => JSON.parseObject(x, classOf[Orders]).user_id)
      .map((_,1L))
      .reduceByKeyAndWindow(_+_,_-_,Seconds(30))
    log.print()
    log.foreachRDD(rdd => {
      rdd.foreach(x=>println(x._1+"====>"+x._2))
    })
//    log.saveAsTextFiles(path)
  }

  /**
    * 获取当前时间
    */
  def getNowDate()={
    val now:Date = new Date()
    val dateFormat:SimpleDateFormat = new SimpleDateFormat("yyyyMMdd")
    val today = dateFormat.format(now)
    today
  }
}
