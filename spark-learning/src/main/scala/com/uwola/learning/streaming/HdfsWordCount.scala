package com.uwola.learning.streaming

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}
// hdfs://master:9000/data   hdfs://master:9000/data/
object HdfsWordCount {
  def main(args: Array[String]): Unit = {
    if (args.length < 2) {
      System.err.println("Usage: HdfsWordCount <directory>")
      System.exit(1)
    }
    // StreamingExamples.setStreamingLogLevels()
    val sparkConf = new SparkConf().setAppName("HdfsWordCount")
//    sparkConf.setMaster("local")
    sparkConf.setMaster("local[3]")
    // Create the context
    val ssc = new StreamingContext(sparkConf, Seconds(5))

    // Create the FileInputDStream on the directory and use the
    // stream to count words in new files created
    //监测到新增的文件
    val lines = ssc.textFileStream(args(0))
    val words = lines.flatMap({x =>
      println("=====>"+x)
      x.split(" ")
    })
    val wordCounts = words.map(x => (x, 1)).reduceByKey(_ + _)
    wordCounts.print()
    wordCounts.saveAsTextFiles(args(1))
    ssc.start()
    ssc.awaitTermination()
  }

}
