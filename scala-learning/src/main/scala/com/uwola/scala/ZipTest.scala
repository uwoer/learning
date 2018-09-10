package com.uwola.scala

/**
  * Created by uwoer on 2018/9/10.
  */
object ZipTest {
  /**
    * 模拟推荐系统中的打分
    * @param args
    */
  def main(args: Array[String]): Unit = {
    // 加权之前的打分分数集合
    val score = Seq(10, 6, 7, 8)
    // 权重集合
    val weight = Seq(0.3, 0.3, 0.2, 0.2)
    // 对两个集合进行拉链操作
    val kvs = score.zip(weight)
    // 得到加权之后的打分分数集合
    val rescores = for ((k, v) <- kvs) yield (k*v)
    // 得到最终的打分
    println(rescores.sum)
  }

}
