package com.uwola.learning.put

import java.util

import com.uwola.learning.conf.ConfUtils
import com.uwola.learning.enums.PhType
import com.uwola.learning.utils.PhTypeUtil
import org.apache.hadoop.hbase.TableName
import org.apache.hadoop.hbase.client.{ConnectionFactory, Put}
import org.apache.spark.SparkContext
import org.apache.spark.sql.SQLContext

/**
  * Created by uwoer on 2018/6/4.
  * 其余增删改查操作可以参考
  * https://www.cnblogs.com/xinfang520/p/7717399.html
  */
object TestPut1 {
  val batchSize = 5
  def put(): Unit = {
    val list: util.ArrayList[util.ArrayList[Put]] = new util.ArrayList[util.ArrayList[Put]]()
    for (a <- 1 to 5){
      var putList: util.ArrayList[Put] = null
      var rowCount:Long = 0
      for (b <- 1 to 20){
        if(rowCount == 0){
          putList = new util.ArrayList[Put]()
        }
        rowCount = rowCount+1
        val p = new Put((a+""+b).getBytes)
        p.addColumn("basicinfo".getBytes, "name".getBytes, "xxxxoooo".getBytes);
        p.addColumn("basicinfo".getBytes, "age".getBytes, PhTypeUtil.toBytes(new Integer(1314),PhType.UNSIGNED_INT));
        putList.add(p)
        if (rowCount % batchSize == 0) {
          //提交
          list.add(putList)
          rowCount = 0
        }
      }
      if (rowCount>0){
        //提交
        list.add(putList)
      }

      println("xxxxxx")
    }

    println("xxxxxxxx")
  }

  def main(args: Array[String]): Unit = {
    put()
  }

}
