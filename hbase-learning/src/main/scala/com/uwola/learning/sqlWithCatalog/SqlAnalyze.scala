package com.uwola.learning.sqlWithCatalog

import org.apache.spark.sql.execution.datasources.hbase.HBaseTableCatalog
import org.apache.spark.sql.{DataFrame, _}

object SqlAnalyze {
  val cat =
    s"""{
       |"table":{"namespace":"default", "name":"visits", "tableCoder":"PrimitiveType"},
       |"rowkey":"key",
       |"columns":{
       |"col0":{"cf":"rowkey", "col":"key", "type":"string"},
       |"col1":{"cf":"basicinfo", "col":"userId", "type":"string"},
       |"col2":{"cf":"basicinfo", "col":"sessionId", "type":"string"},
       |"col3":{"cf":"basicinfo", "col":"sendTime", "type":"long"},
       |"col4":{"cf":"basicinfo", "col":"eventTime", "type":"long"},
       |"col5":{"cf":"basicinfo", "col":"eventType", "type":"string"},
       |"col6":{"cf":"basicinfo", "col":"login_userId", "type":"string"},
       |"col7":{"cf":"basicinfo", "col":"id", "type":"string"},
       |"col8":{"cf":"basicinfo", "col":"mobile", "type":"string"},
       |
       |"col9":{"cf":"location", "col":"ip", "type":"string"},
       |"col10":{"cf":"location", "col":"countryName", "type":"string"},
       |"col11":{"cf":"location", "col":"region", "type":"string"},
       |"col12":{"cf":"location", "col":"city", "type":"string"},
       |"col13":{"cf":"location", "col":"lat", "type":"double"},
       |"col14":{"cf":"location", "col":"lng", "type":"double"},
       |
       |"col15":{"cf":"device", "col":"model", "type":"string"},
       |"col16":{"cf":"device", "col":"manufacturer", "type":"string"},
       |"col17":{"cf":"device", "col":"resolution", "type":"string"},
       |"col18":{"cf":"device", "col":"osVersion", "type":"string"},
       |"col19":{"cf":"device", "col":"platform", "type":"string"},
       |
       |"col20":{"cf":"misc", "col":"domain", "type":"string"},
       |"col21":{"cf":"misc", "col":"path", "type":"string"},
       |"col22":{"cf":"misc", "col":"refer", "type":"string"},
       |"col23":{"cf":"misc", "col":"userAgent", "type":"string"},
       |"col24":{"cf":"misc", "col":"appVersion", "type":"string"},
       |"col25":{"cf":"misc", "col":"channel", "type":"string"},
       |"col26":{"cf":"misc", "col":"language", "type":"string"},
       |"col27":{"cf":"misc", "col":"query", "type":"string"},
       |"col28":{"cf":"misc", "col":"companyId", "type":"string"},
       |"col29":{"cf":"misc", "col":"projectId", "type":"string"}
       |}
       |}""".stripMargin

  val cat1 =
    s"""{
       |"table":{"namespace":"default", "name":"visits", "tableCoder":"PrimitiveType"},
       |"rowkey":"key",
       |"columns":{
       |"rowkey":{"cf":"rowkey", "col":"key", "type":"string"},
       |"userId":{"cf":"basicinfo", "col":"userId", "type":"string"},
       |"sessionId":{"cf":"basicinfo", "col":"sessionId", "type":"string"},
       |"sendTime":{"cf":"basicinfo", "col":"sendTime", "type":"long"},
       |"eventTime":{"cf":"basicinfo", "col":"eventTime", "type":"long"},
       |"eventType":{"cf":"basicinfo", "col":"eventType", "type":"string"},
       |"login_userId":{"cf":"basicinfo", "col":"login_userId", "type":"string"},
       |"id":{"cf":"basicinfo", "col":"id", "type":"string"},
       |"mobile":{"cf":"basicinfo", "col":"mobile", "type":"string"},
       |
       |"ip":{"cf":"location", "col":"ip", "type":"string"},
       |"countryName":{"cf":"location", "col":"countryName", "type":"string"},
       |"region":{"cf":"location", "col":"region", "type":"string"},
       |"city":{"cf":"location", "col":"city", "type":"string"},
       |"lat":{"cf":"location", "col":"lat", "type":"double"},
       |"lng":{"cf":"location", "col":"lng", "type":"double"},
       |
       |"model":{"cf":"device", "col":"model", "type":"string"},
       |"manufacturer":{"cf":"device", "col":"manufacturer", "type":"string"},
       |"resolution":{"cf":"device", "col":"resolution", "type":"string"},
       |"osVersion":{"cf":"device", "col":"osVersion", "type":"string"},
       |"platform":{"cf":"device", "col":"platform", "type":"string"},
       |
       |"domain":{"cf":"misc", "col":"domain", "type":"string"},
       |"path":{"cf":"misc", "col":"path", "type":"string"},
       |"refer":{"cf":"misc", "col":"refer", "type":"string"},
       |"userAgent":{"cf":"misc", "col":"userAgent", "type":"string"},
       |"appVersion":{"cf":"misc", "col":"appVersion", "type":"string"},
       |"channel":{"cf":"misc", "col":"channel", "type":"string"},
       |"language":{"cf":"misc", "col":"language", "type":"string"},
       |"query":{"cf":"misc", "col":"query", "type":"string"},
       |"companyId":{"cf":"misc", "col":"companyId", "type":"string"},
       |"projectId":{"cf":"misc", "col":"projectId", "type":"string"}
       |}
       |}""".stripMargin

  def runAction(actionType: String, hBaseDataFrame: DataFrame, action: DataFrame => Unit): Unit = {

    action(hBaseDataFrame)

  }

  def main(args: Array[String]) {
    val spark = SparkSession.builder().appName("SqlAnalyze")
      .master("local")
      .getOrCreate()

    val sc = spark.sparkContext
    val sqlContext = spark.sqlContext

    def withCatalog(cat: String): DataFrame = {
      sqlContext
        .read
        .options(Map(HBaseTableCatalog.tableCatalog -> cat))
        .format("org.apache.spark.sql.execution.datasources.hbase")
        .load()
    }
//    val df = withCatalog(cat)
    val df = withCatalog(cat1)
    println("table count is " + df.count())

    //1.filter scan
    val startTime = System.currentTimeMillis()
    df.show()
    /*val countDf = df.filter($"col0" <= "row005").filter($"col1")
      .select($"col0", $"col1")
    val countResult = countDf.count
    println("filter scan count:" + countResult)*/
    val endTime = System.currentTimeMillis()
    println("action cost time:" + ((endTime - startTime)/1000.0) + "s")

    spark.stop()
  }
}
