package com.uwola.learning.scan

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.ArrayList

import com.uwola.learning.conf.ConfUtils
import com.uwola.learning.redis.RedisClient
import org.apache.hadoop.hbase.TableName
import org.apache.hadoop.hbase.client.{ConnectionFactory, Put, Result, Scan}
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.hadoop.hbase.util.Bytes
import org.apache.spark.sql.SparkSession

/**
  * 更新visits表
  * Created by uwoer on 2018/6/4.
  */
object VisitsUpdate {
  private val TABLE_NAME = "visits"
  val batchSize = 2000
   def update(spark: SparkSession, start:LocalDateTime, end:LocalDateTime): Unit = {
     val hBaseConf = ConfUtils.createHBaseConf()
     hBaseConf.set(TableInputFormat.INPUT_TABLE, TABLE_NAME)

     val scan = new Scan()
     scan.setBatch(1000)
     //用来指定显示的列以及条件列
     scan.addColumn(Bytes.toBytes("basicinfo"), Bytes.toBytes("login_userId"))
     scan.setStartRow(Bytes.toBytes(start.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
     scan.setStopRow(Bytes.toBytes(end.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
     //把过滤器加载到hbaseconf中
     hBaseConf.set(TableInputFormat.SCAN, ConfUtils.convertScanToString(scan))
     //构建RDD
     val hBaseRDD = spark.sparkContext.newAPIHadoopRDD(
       hBaseConf,
       classOf[TableInputFormat],
       classOf[ImmutableBytesWritable], classOf[Result])


     hBaseRDD.foreachPartition(partitionRecords =>{
       val conf = ConfUtils.createHBaseConf()
       //Connection 的创建是个重量级的工作，线程安全，是操作hbase的入口
       val conn = ConnectionFactory.createConnection(conf)
       val table = conn.getTable(TableName.valueOf(TABLE_NAME))
       var putList: ArrayList[Put] = null
       val jedis = RedisClient.pool.getResource
       var rowCount:Long = 0
       partitionRecords.foreach(r=>{
         val login_userId: String = Bytes.toString(r._2.getValue(Bytes.toBytes("basicinfo"), Bytes.toBytes("login_userId")))
         val mobile = jedis.get("USER_MAIN_"+login_userId)
         if(mobile!=null && !"".equals(mobile)){
           if(rowCount == 0){
             putList = new ArrayList[Put]()
           }
           //这个是rowKey
           val p = new Put(r._1.copyBytes())
           p.addColumn(Bytes.toBytes("basicinfo"),Bytes.toBytes("mobile"), Bytes.toBytes(mobile))
           putList.add(p)
           rowCount = rowCount+1
           if (rowCount % batchSize == 0) {
             //提交
             table.put(putList)
             rowCount = 0
           }
         }
       })
       if (rowCount > 0) {
         //提交
         table.put(putList)
       }
       if(table != null) table.close()
       jedis.close()
     })
  }
}
