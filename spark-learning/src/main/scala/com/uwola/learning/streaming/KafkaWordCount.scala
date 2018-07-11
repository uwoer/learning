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
    // 举个列子在在非driect模式下消费kafka 如果有3台服务器  每台机子开启一个进程，每个进程开启多少个线程合适[负载均衡]
    //每台机子应用有6个线程  其中有两台机子的线程会被打满 另一台机子有一个线程富余
    //这里有一个原则就是在创建kafka topic时指定的partition数n尽量是一个有较多公约数的数字 比如12 它的公约数（2,3,4,6） 这样在指定线程是相对容易
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
