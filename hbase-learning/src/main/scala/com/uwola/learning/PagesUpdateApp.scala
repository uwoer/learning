package com.uwola.learning

import java.text.DecimalFormat
import java.time.format.DateTimeParseException
import java.time.{LocalDate, LocalDateTime, ZoneId}

import com.uwola.learning.scan.PagesUpdate
import org.apache.spark.sql.SparkSession

/**
 * Hello world!
 */
object PagesUpdateApp{
  val FORMAT_2 = new DecimalFormat("##.##")
  def main(args: Array[String]): Unit = {
    val startNanoTime = System.nanoTime()
    //默认无参数为生产定时任务
    var startDate = LocalDate.now(ZoneId.of("Asia/Shanghai")).minusDays(1L)
    var endDate = LocalDate.now(ZoneId.of("Asia/Shanghai")).minusDays(1L)
    println( "This is a spark with hbase hello world app !" )
    if (args.length == 2) {
      //指定时间及环境
      try{
        System.out.println("startDate ===>" + args(0))
        startDate = LocalDate.parse(args(0))
      } catch {
        case e: DateTimeParseException => {
          System.out.println("Wrong start date format! Example: 2017-07-01")
          System.exit(1)
        }
      }
      try{
        System.out.println("endDate ===>" + args(1))
        endDate = LocalDate.parse(args(1))
      } catch {
        case e: DateTimeParseException => {
          System.out.println("Wrong end date format! Example: 2017-07-01")
          System.exit(1)
        }
      }
    }else if(args.length == 0){

    }else{
      System.err.println(s"""
                            |Usage: Update Hbase data Args <start> <end>
                            |  <startDate> the startDate (include) to statistic analysis
                            |  <endDate>   the endDate (not include) to statistic analysis
        """.stripMargin)
      System.exit(1)
    }
    val start: LocalDateTime = startDate.atStartOfDay
    val end: LocalDateTime = endDate.plusDays(1L).atStartOfDay
    val gcOptions =
      """
        |-XX:+UseG1GC
        |-XX:+UnlockDiagnosticVMOptions
        |-XX:+G1SummarizeConcMark
        |-XX:InitiatingHeapOccupancyPercent=35
        |-XX:InitiatingHeapOccupancyPercent=65
        |-XX:+ParallelRefProcEnabled
        |-XX:ConcGCThreads=4
        |-XX:ParallelGCThreads=16
        |-XX:MaxTenuringThreshold=1
        |-XX:G1HeapRegionSize=32m
        |-XX:G1MixedGCCountTarget=64
        |-XX:G1OldCSetRegionThresholdPercent=5
        |-verbose:gc
        |-XX:+PrintGC
        |-XX:+PrintGCDetails
        |-XX:+PrintGCApplicationStoppedTime
        |-XX:+PrintHeapAtGC
        |-XX:+PrintGCDateStamps
        |-XX:+PrintAdaptiveSizePolicy
        |-XX:+PrintTenuringDistribution
        |-XX:+PrintSafepointStatistics
        |-XX:PrintSafepointStatisticsCount=1
        |-XX:PrintFLSStatistics=1
        |-XX:+PrintFlagsFinal
        |-XX:+PrintReferenceGC
      """.stripMargin
    println(gcOptions)
    val spark = SparkSession
      .builder()
//      .appName("Spark Hbase Example")
      .appName("Spark Hbase Data Completion APP 4 Pages")
      .config("spark.hbase.host", "172.31.5.30")
      //解决堆外空间不足的问题
      .config("spark.yarn.executor.memoryOverhead", "1024")
//      .config("spark.executor.extraJavaOptions", gcOptions)
//本地测试时打开
//      .master("local")
//Standlone集群测试时打开
//      .master("spark://master:7077")
      //调试executor时打开
//      .config("spark.executor.extraJavaOptions", "-Xdebug -Xrunjdwp:transport=dt_socket,address=10001,server=y,suspend=n")
      .getOrCreate()

      PagesUpdate.update(spark, start, end)

//关闭SparkSession上下文 释放资源
    spark.stop()
    println("=====================》task takes "+takes(startNanoTime)+"s")
  }
  /**
    * 任务耗时统计
    */
  def takes(startNanoTime: Long) = {
    val takes:Double = System.nanoTime - startNanoTime;
    FORMAT_2.format(takes / (1000 * 1000 * 1000));
  }

}
