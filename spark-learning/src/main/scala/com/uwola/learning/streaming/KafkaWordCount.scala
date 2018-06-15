package com.uwola.learning.streaming

import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Minutes, Seconds, StreamingContext}
//zkQuorum: master:2181,slave1:2181,slave2:2181
//group: badou111
//topic: test
//numThreads:1
//master:2181,slave1:2181,slave2:2181 badou111  test  1

object KafkaWordCount {
  def main(args: Array[String]): Unit = {
    if (args.length < 4) {
      System.err.println("Usage: KafkaWordCount <zkQuorum> <group> <topics> <numThreads>")
      System.exit(1)
    }

    val Array(zkQuorum, group, topics, numThreads) = args
    val sparkConf = new SparkConf().setAppName("KafkaWordCount")
    sparkConf.setMaster("local[3]")
    val ssc = new StreamingContext(sparkConf, Seconds(5))
    ssc.checkpoint("checkpoint")

    val topicMap = topics.split(",").map((_, numThreads.toInt)).toMap
    val lines = KafkaUtils.createStream(ssc, zkQuorum, group, topicMap).map(_._2)
    val words = lines.flatMap(_.split(" "))
    val wordCounts = words.map(x => (x, 1L))
      .reduceByKeyAndWindow(_ + _, _ - _, Minutes(10), Seconds(5), 2)
    wordCounts.print()

    ssc.start()
    ssc.awaitTermination()
  }

}
