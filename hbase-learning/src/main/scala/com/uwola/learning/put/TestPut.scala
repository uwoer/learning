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
object TestPut {
  val TABLE_PERSON = TableName.valueOf("person")
  def put(sc: SparkContext, sqlContext :SQLContext): Unit = {
    val hBaseConf = ConfUtils.createHBaseConf()
    val conn = ConnectionFactory.createConnection(hBaseConf)
    val table = conn.getTable(TABLE_PERSON)
    try {
      val putList: util.ArrayList[Put] = new util.ArrayList[Put]()
      val p = new Put(("11").getBytes)
      p.addColumn("basicinfo".getBytes, "name".getBytes, "xxxxoooo".getBytes);
      p.addColumn("basicinfo".getBytes, "age".getBytes, PhTypeUtil.toBytes(new Integer(1314),PhType.UNSIGNED_INT));
      putList.add(p)
      //提交
      table.put(putList)
    } finally {
        if(table != null) table.close()
    }
  }

}
