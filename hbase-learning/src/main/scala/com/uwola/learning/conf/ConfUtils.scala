package com.uwola.learning.conf


import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.hbase.HBaseConfiguration
import org.apache.hadoop.hbase.client.Scan
import org.apache.hadoop.hbase.protobuf.ProtobufUtil
import org.apache.hadoop.hbase.util.Base64

/**
  * 配置信息工具类
  * Created by uwoer on 2018/6/4.
  */
object ConfUtils {

  def createHBaseConf(): Configuration ={
    val hBaseConf = HBaseConfiguration.create()
    hBaseConf.set("hbase.zookeeper.property.clientPort", "2181")
    hBaseConf.set("hbase.zookeeper.quorum", "172.31.5.30") //Change the EMR master IP addr. to localhost when you debug locally
//    hBaseConf.set("hbase.zookeeper.quorum", "master,slave1,slave2")
    hBaseConf.set("hbase.client.retries.number", "60") //重试次数  见org.apache.hadoop.hbase.client.ClientScanner
    hBaseConf.set("hbase.client.scanner.timeout.period", (60000*10)+"") //超时时间
    //Hbase客户端和regionserver之间的连接数
    //https://blog.csdn.net/yangbutao/article/details/8585841
//    hBaseConf.set("hbase.client.ipc.pool.size", "1")
    return  hBaseConf
  }

  /**
    * 见
    * org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil.convertScanToString
    */
  def convertScanToString(scan: Scan) = {
    val proto = ProtobufUtil.toScan(scan)
    Base64.encodeBytes(proto.toByteArray)
  }

}
