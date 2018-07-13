package com.uwola.learning.scan


import com.uwola.learning.conf.ConfUtils
import com.uwola.learning.enums.PhType
import com.uwola.learning.utils.PhTypeUtil
import org.apache.hadoop.hbase.client.{Result, Scan}
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.hadoop.hbase.util.Bytes
import org.apache.spark.SparkContext
import org.apache.spark.sql.SQLContext

/**
  * 扫描
  * Created by uwoer on 2018/6/4.
  */
object TestScanner {
  private val TABLE_NAME = "person"
   def scanner(sc: SparkContext, sqlContext :SQLContext): Unit = {
     val hBaseConf = ConfUtils.createHBaseConf()
     hBaseConf.set(TableInputFormat.INPUT_TABLE, TABLE_NAME)

     val scan = new Scan()
     //用来指定显示的列以及条件列
     scan.addColumn(Bytes.toBytes("basicinfo"), Bytes.toBytes("name"))
     scan.addColumn(Bytes.toBytes("basicinfo"), Bytes.toBytes("age"))

     import sqlContext.implicits._
     //把过滤器加载到hbaseconf中
     hBaseConf.set(TableInputFormat.SCAN, ConfUtils.convertScanToString(scan))
     //构建RDD
     val hBaseRDD = sc.newAPIHadoopRDD(
       hBaseConf,
       classOf[TableInputFormat],
       classOf[ImmutableBytesWritable], classOf[Result])

     // 将数据映射为表  也就是将 RDD转化为 dataframe schema

     //单元测试时打印df中所有需要关注的字段
    val eventsRdd = hBaseRDD.map(r =>{
//      println(r._2)
      (
        Bytes.toString(r._2.getValue(Bytes.toBytes("basicinfo"), Bytes.toBytes("name"))),//已建索引
//        Bytes.toInt(r._2.getValue(Bytes.toBytes("basicinfo"), Bytes.toBytes("age")))//已建索引
        Integer.valueOf(PhTypeUtil.toObject(r._2.getValue(Bytes.toBytes("basicinfo"), Bytes.toBytes("age")),PhType.UNSIGNED_INT).toString)//已建索引
      )
      }
     ).toDF("name", "age").show()

    /* val rdd = hBaseRDD.map(r => (
       (
         Bytes.toString(r._2.getValue(Bytes.toBytes("basicinfo"), Bytes.toBytes("userId")))+
           Bytes.toString(r._2.getValue(Bytes.toBytes("basicinfo"), Bytes.toBytes("login_userId")))
         )
       ,
       (
         Bytes.toString(r._2.getValue(Bytes.toBytes("basicinfo"), Bytes.toBytes("userId"))),
         Bytes.toString(r._2.getValue(Bytes.toBytes("basicinfo"), Bytes.toBytes("login_userId")))
       )
     )).reduceByKey((x,y)=>x).map(_._2).toDF("deviceid", "loginid")*/
  }
}
