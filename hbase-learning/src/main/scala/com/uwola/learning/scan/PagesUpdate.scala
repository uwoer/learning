package com.uwola.learning.scan

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.ArrayList

import com.uwola.learning.conf.ConfUtils
import com.uwola.learning.enums.PhType
import com.uwola.learning.utils.PhTypeUtil
import org.apache.hadoop.hbase.TableName
import org.apache.hadoop.hbase.client.{ConnectionFactory, Put, Result, Scan}
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.hadoop.hbase.util.Bytes
import org.apache.spark.sql.{DataFrame, SparkSession}

/**
  * 更新pages表
  * Created by uwoer on 2018/6/4.
  */
object PagesUpdate {
  private val TABLE_NAME_VISITS = "visits"
  private val TABLE_NAME_PAGES = "pages"
  val batchSize = 2000
   def update(spark: SparkSession, start:LocalDateTime, end:LocalDateTime): Unit = {
     val visitsDf = visits2Df(spark, start, end)
     val pagesDf = pages2Df(spark, start, end)
     /*pagesDf.join(visitsDf,"visit_id").select(
       "pk",
       "ip",
       "countryName",
       "city",
       "lat",
       "lng",
       "model",
       "manufacturer",
       "resolution",
       "osVersion",
       "appVersion",
       "channel",
       "language"
     ).show(5)*/
     pagesDf.join(visitsDf,"visit_id").select(
       "pk",
       "ip",
       "countryName",
       "city",
       "lat",
       "lng",
       "model",
       "manufacturer",
       "resolution",
       "osVersion",
       "appVersion",
       "channel",
       "language"
     ).foreachPartition(partitionRecords =>{
       val conf = ConfUtils.createHBaseConf()
       //Connection 的创建是个重量级的工作，线程安全，是操作hbase的入口
       val conn = ConnectionFactory.createConnection(conf)
       val table = conn.getTable(TableName.valueOf(TABLE_NAME_PAGES))
       var putList: ArrayList[Put] = null
       var rowCount:Long = 0
       partitionRecords.foreach(row=>{
         var be = false
         val p = new Put(Bytes.toBytes(row.getString(0)))
         if(!row.isNullAt(1)){
           p.addColumn(Bytes.toBytes("location"), Bytes.toBytes("ip"), Bytes.toBytes(row.getString(1)))
           be = true
         }
         if(!row.isNullAt(2)){
           p.addColumn(Bytes.toBytes("location"), Bytes.toBytes("countryName"), Bytes.toBytes(row.getString(2)))
           be = true
         }
         if(!row.isNullAt(3)){
           p.addColumn(Bytes.toBytes("location"), Bytes.toBytes("city"), Bytes.toBytes(row.getString(3)))
           be = true
         }
         if(!row.isNullAt(4)){
           p.addColumn(Bytes.toBytes("location"), Bytes.toBytes("lat"), PhTypeUtil.toBytes(row.getDouble(4),PhType.UNSIGNED_DOUBLE))
           be = true
         }
         if(!row.isNullAt(5)){
           p.addColumn(Bytes.toBytes("location"), Bytes.toBytes("lng"), PhTypeUtil.toBytes(row.getDouble(5),PhType.UNSIGNED_DOUBLE))
           be = true
         }

         if(!row.isNullAt(6)){
           p.addColumn(Bytes.toBytes("device"), Bytes.toBytes("model"), Bytes.toBytes(row.getString(6)))
           be = true
         }
         if(!row.isNullAt(7)){
           p.addColumn(Bytes.toBytes("device"), Bytes.toBytes("manufacturer"), Bytes.toBytes(row.getString(7)))
           be = true
         }
         if(!row.isNullAt(8)){
           p.addColumn(Bytes.toBytes("device"), Bytes.toBytes("resolution"), Bytes.toBytes(row.getString(8)))
           be = true
         }
         if(!row.isNullAt(9)){
           p.addColumn(Bytes.toBytes("device"), Bytes.toBytes("osVersion"), Bytes.toBytes(row.getString(9)))
           be = true
         }

         if(!row.isNullAt(10)){
           p.addColumn(Bytes.toBytes("misc"), Bytes.toBytes("appVersion"), Bytes.toBytes(row.getString(10)))
           be = true
         }
         if(!row.isNullAt(11)){
           p.addColumn(Bytes.toBytes("misc"), Bytes.toBytes("channel"), Bytes.toBytes(row.getString(11)))
           be = true
         }
         if(!row.isNullAt(12)){
           p.addColumn(Bytes.toBytes("misc"), Bytes.toBytes("language"), Bytes.toBytes(row.getString(12)))
           be = true
         }
         if(be){
           if(rowCount == 0){
             putList = new ArrayList[Put]()
           }
           putList.add(p)
           rowCount = rowCount+1
           if (rowCount % batchSize == 0) {
             //提交
             table.put(putList)
             rowCount = 0
             //gc help
             putList = null
           }
         }

       })
       if (rowCount > 0) {
         //提交
         table.put(putList)
         //gc help
         putList = null
       }
       if(table != null) table.close()
     })
  }



  def visits2Df(spark: SparkSession, start:LocalDateTime, end:LocalDateTime): DataFrame = {
    val hBaseConf = ConfUtils.createHBaseConf()
    hBaseConf.set(TableInputFormat.INPUT_TABLE, TABLE_NAME_VISITS)

    val scan = new Scan()
    scan.setBatch(1000)
//    scan.setCacheBlocks(false)
    //用来指定显示的列以及条件列
    scan.addColumn(Bytes.toBytes("basicinfo"), Bytes.toBytes("id"))
    scan.addColumn(Bytes.toBytes("location"), Bytes.toBytes("ip"))
    scan.addColumn(Bytes.toBytes("location"), Bytes.toBytes("countryName"))
    scan.addColumn(Bytes.toBytes("location"), Bytes.toBytes("city"))
    scan.addColumn(Bytes.toBytes("location"), Bytes.toBytes("lat"))
    scan.addColumn(Bytes.toBytes("location"), Bytes.toBytes("lng"))

    scan.addColumn(Bytes.toBytes("device"), Bytes.toBytes("model"))
    scan.addColumn(Bytes.toBytes("device"), Bytes.toBytes("manufacturer"))
    scan.addColumn(Bytes.toBytes("device"), Bytes.toBytes("resolution"))
    scan.addColumn(Bytes.toBytes("device"), Bytes.toBytes("osVersion"))

    scan.addColumn(Bytes.toBytes("misc"), Bytes.toBytes("appVersion"))
    scan.addColumn(Bytes.toBytes("misc"), Bytes.toBytes("channel"))
    scan.addColumn(Bytes.toBytes("misc"), Bytes.toBytes("language"))
    //放宽一天
    scan.setStartRow(Bytes.toBytes(start.plusDays(-1L).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
    scan.setStopRow(Bytes.toBytes(end.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))

    //把过滤器加载到hbaseconf中
    hBaseConf.set(TableInputFormat.SCAN, ConfUtils.convertScanToString(scan))
    //构建RDD
    val hBaseRDD = spark.sparkContext.newAPIHadoopRDD(
      hBaseConf,
      classOf[TableInputFormat],
      classOf[ImmutableBytesWritable], classOf[Result])

    import spark.implicits._
    val df = hBaseRDD.map(r =>{
      var lat_Double:Double = 0
      var lng_Double:Double = 0
      val lat = r._2.getValue(Bytes.toBytes("location"), Bytes.toBytes("lat"))
      val lng = r._2.getValue(Bytes.toBytes("location"), Bytes.toBytes("lng"))
      if(lat!=null){
        lat_Double = Bytes.toDouble(lat)
      }
      if(lng!=null){
        lng_Double = Bytes.toDouble(lng)
      }
      (
        Bytes.toString(r._2.getValue(Bytes.toBytes("basicinfo"), Bytes.toBytes("id"))),
        Bytes.toString(r._2.getValue(Bytes.toBytes("location"), Bytes.toBytes("ip"))),
        Bytes.toString(r._2.getValue(Bytes.toBytes("location"), Bytes.toBytes("countryName"))),
        Bytes.toString(r._2.getValue(Bytes.toBytes("location"), Bytes.toBytes("city"))),
        lat_Double,
        lng_Double,

        Bytes.toString(r._2.getValue(Bytes.toBytes("device"), Bytes.toBytes("model"))),
        Bytes.toString(r._2.getValue(Bytes.toBytes("device"), Bytes.toBytes("manufacturer"))),
        Bytes.toString(r._2.getValue(Bytes.toBytes("device"), Bytes.toBytes("resolution"))),
        Bytes.toString(r._2.getValue(Bytes.toBytes("device"), Bytes.toBytes("osVersion"))),

        Bytes.toString(r._2.getValue(Bytes.toBytes("misc"), Bytes.toBytes("appVersion"))),
        Bytes.toString(r._2.getValue(Bytes.toBytes("misc"), Bytes.toBytes("channel"))),
        Bytes.toString(r._2.getValue(Bytes.toBytes("misc"), Bytes.toBytes("language")))
      )
    }).toDF(
      "visit_id",
      "ip",
      "countryName",
      "city",
      "lat",
      "lng",
      "model",
      "manufacturer",
      "resolution",
      "osVersion",
      "appVersion",
      "channel",
      "language")

//    df.show(10)
    df
  }

  def pages2Df(spark: SparkSession, start:LocalDateTime, end:LocalDateTime): DataFrame = {
    val hBaseConf = ConfUtils.createHBaseConf()
    hBaseConf.set(TableInputFormat.INPUT_TABLE, TABLE_NAME_PAGES)

    val scan = new Scan()
    scan.setBatch(1000)
//    scan.setCacheBlocks(false)
    //用来指定显示的列以及条件列
    scan.addColumn(Bytes.toBytes("basicinfo"), Bytes.toBytes("visit_id"))
    scan.setStartRow(Bytes.toBytes(start.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
    scan.setStopRow(Bytes.toBytes(end.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))

    //把过滤器加载到hbaseconf中
    hBaseConf.set(TableInputFormat.SCAN, ConfUtils.convertScanToString(scan))
    //构建RDD
    val hBaseRDD = spark.sparkContext.newAPIHadoopRDD(
      hBaseConf,
      classOf[TableInputFormat],
      classOf[ImmutableBytesWritable], classOf[Result])

    import spark.implicits._
    hBaseRDD.map(r =>{
      (
        Bytes.toString(r._1.copyBytes()),
        Bytes.toString(r._2.getValue(Bytes.toBytes("basicinfo"), Bytes.toBytes("visit_id")))
      )
    }).toDF("pk", "visit_id")

  }
}
