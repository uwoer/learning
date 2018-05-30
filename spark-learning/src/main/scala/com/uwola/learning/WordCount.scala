package com.uwola.learning

import org.apache.spark.{SparkConf, SparkContext}

/**
  * spark word count helloword
  * 注意本例子的主要目的是锻炼各种函数的使用
  * Created by uwoer on 2018/5/3.
  */
object WordCount {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("").setMaster("local")
//    val conf = new SparkConf().setAppName("spark WordCount")
    val sc = new SparkContext(conf)
    val lines = sc.textFile("hdfs://master:9000/data/The_Man_of_Property.txt")
//    lines.flatMap(line => line.split(" ")).filter(x => x.matches("\\w+")).map(word => (word,1)).reduceByKey(_ + _).map(x => (x._2,x._1)).sortByKey(false).map(x => (x._2,x._1)).foreach({x => println("key==>"+x._1+",value==>"+x._2)})
//    lines.flatMap(line => line.split(" ")).filter(x => x.matches("\\w+")).map(word => (word,1)).reduceByKey(_ + _).map(x => (x._2,x._1)).sortByKey(false).map(x => (x._2,x._1)).saveAsTextFile("hdfs://master:9000/data/out")
//    lines.flatMap(line => line.split(" ")).filter(x => x.matches("\\w+")).map((_,1)).reduceByKey(_ + _).map(x => (x._2,x._1)).sortByKey(false).map(x => (x._2,x._1)).saveAsTextFile("hdfs://master:9000/data/out")

//    val lines = Source.fromFile("The_man_of_property.txt").getLines().toList
    //打平数据
//    lines.map(_.split(" ")).flatten
    lines.flatMap(_.split(" ")).map((_,1)).groupBy(_._1).map(x=>(x._1,x._2.size)).foreach(println)
    lines.flatMap(_.split(" ")).map((_,1)).groupBy(_._1).mapValues(_.size).foreach(println)
    //这个在scala命令行中可以执行  但是在idea中无法执行
    //lines.flatMap(_.split(" ")).map((_,1)).groupBy(_._1).map(x=>(x._1,x._2.length)).foreach(println)

    //如果不通过list大小来算具体单词的次数（词频）
    lines.flatMap(_.split(" ")).map((_,1)).groupBy(_._1).map(x=>(x._1,x._2.map(_._2).reduce(_+_))).foreach(println)
    lines.flatMap(_.split(" ")).map((_,1)).groupBy(_._1).map(x=>(x._1,x._2.map(_._2).sum)).foreach(println)

    //sortBy(_._2)  指定按tupe的第二个元素排序
    //reverse 反转数组
    //slice(0,10) 切出top 10
    lines.flatMap(_.split(" ")).map((_,1)).groupBy(_._1).mapValues(_.size).collect().toList.sortBy(_._2).reverse.slice(0,10).foreach(println)
    lines.flatMap(_.split(" ")).map((_,1)).groupBy(_._1).mapValues(_.size).collect().toList.sortWith(_._2>_._2).slice(0,10).foreach(println)

    sc.stop()
  }

}
