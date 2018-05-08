package com.uwola.learning

import org.apache.spark.sql.SparkSession

/**
 * Hello world!
 *
 */
object App{
  def main(args: Array[String]): Unit = {
    println( "This is a sparksql with hive hello world app !" )

    val spark = SparkSession
      .builder()
      .appName("Spark Hive Example")
//本地测试时打开
//      .master("local")
//Standlone集群测试时打开
//      .master("spark://master:7077")
      .enableHiveSupport()
      .getOrCreate()

    import spark.sql

    sql("show databases").show

//关闭SparkSession上下文 释放资源
    spark.stop()

  }

}
