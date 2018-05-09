package com.uwola.learning

import org.apache.spark.sql.{SaveMode, SparkSession}

import scala.util.Random

/**
 * Hello world!
 * Spark Sql 官网demo 验证
 * （依赖hive-site.xml）
 */
object App{
  case class Record(key: Int, value: String)

  def main(args: Array[String]): Unit = {
    println( "This is a sparksql with hive hello world app !" )

    val spark = SparkSession
      .builder()
      .appName("Spark Hive Example")
//本地测试时打开
      .master("local")
//Standlone集群测试时打开
//      .master("spark://master:7077")
      .enableHiveSupport()
      .getOrCreate()

    import spark.sql

    sql("show databases").show

    import spark.implicits._

    sql("show databases").show()
    sql("use uwola")
    sql("CREATE TABLE IF NOT EXISTS src (key INT, value STRING)")
    //local
    sql("LOAD DATA LOCAL INPATH '/C:/app/spark-2.0.2-bin-hadoop2.6/examples/src/main/resources/kv1.txt' overwrite INTO TABLE src")
    //Standlone
    //....
    //yarn
    //....
    // Queries are expressed in HiveQL
    sql("SELECT * FROM src").show()
    // +---+-------+
    // |key|  value|
    // +---+-------+
    // |238|val_238|
    // | 86| val_86|
    // |311|val_311|
    // ...

    // Aggregation queries are also supported.
    sql("SELECT COUNT(*) FROM src").show()
    // +--------+
    // |count(1)|
    // +--------+
    // |    500 |
    // +--------+

    // The results of SQL queries are themselves DataFrames and support all normal functions.
    val sqlDF = sql("SELECT key, value FROM src WHERE key < 10 ORDER BY key")

    // The items in DataFrames are of type Row, which allows you to access each column by ordinal.
    val stringsDS = sqlDF.map {
      //      case Row(key: Int, value: String) => s"Key: $key, Value: $value"
      r => s"Key: ${r.get(0)}, Value: ${r.get(1)}"
    }
    stringsDS.show()
    // +--------------------+
    // |               value|
    // +--------------------+
    // |Key: 0, Value: val_0|
    // |Key: 0, Value: val_0|
    // |Key: 0, Value: val_0|
    // ...

    // You can also use DataFrames to create temporary views within a SparkSession.
    val recordsDF = spark.createDataFrame((1 to 100).map(i => Record(i, s"val_$i")))
    recordsDF.createOrReplaceTempView("records")

    // Queries can then join DataFrame data with data stored in Hive.
    sql("SELECT * FROM records r JOIN src s ON r.key = s.key").show()
    // +---+------+---+------+
    // |key| value|key| value|
    // +---+------+---+------+
    // |  2| val_2|  2| val_2|
    // |  4| val_4|  4| val_4|
    // |  5| val_5|  5| val_5|
    // ...

    // Create a Hive managed Parquet table, with HQL syntax instead of the Spark SQL native syntax
    // `USING hive`
    sql("CREATE TABLE IF NOT EXISTS hive_records(key int, value string) STORED AS PARQUET")
    // Save DataFrame to the Hive managed table
    val df = spark.table("src")
    df.write.mode(SaveMode.Overwrite).saveAsTable("hive_records")
    // After insertion, the Hive managed table has data now
    sql("SELECT * FROM hive_records").show()
    // +---+-------+
    // |key|  value|
    // +---+-------+
    // |238|val_238|
    // | 86| val_86|
    // |311|val_311|
    // ...

    // Prepare a Parquet data directory
    val tempSuffix = Random.nextLong().toString.replace("-","")
    val dataDir = "hdfs://master:9000/tmp/parquet_data"+tempSuffix
    spark.range(10).write.parquet(dataDir)
    // Create a Hive external Parquet table
    sql(s"CREATE EXTERNAL TABLE IF NOT EXISTS hive_ints(id bigint) STORED AS PARQUET LOCATION '$dataDir'")
    //    sql(s"load data inpath '$dataDir/part-r-00000*' overwrite  into table hive_ints")
    // The Hive external table should already have data
    sql("SELECT * FROM hive_ints").show()
    // +---+
    // |key|
    // +---+
    // |  0|
    // |  1|
    // |  2|
    // ...

    // Turn on flag for Hive Dynamic Partitioning
    spark.sqlContext.setConf("hive.exec.dynamic.partition", "true")
    spark.sqlContext.setConf("hive.exec.dynamic.partition.mode", "nonstrict")
    // Create a Hive partitioned table using DataFrame API
    df.write.partitionBy("key").saveAsTable("hive_part_tbl")
    // Partitioned column `key` will be moved to the end of the schema.
    sql("SELECT * FROM hive_part_tbl").show()
    // +-------+---+
    // |  value|key|
    // +-------+---+
    // |val_238|238|
    // | val_86| 86|
    // |val_311|311|
    // ...

//关闭SparkSession上下文 释放资源
    spark.stop()

  }

}
