package com.uwola.scala

import scala.collection.mutable.ArrayBuffer
import scala.util.Sorting

/**
  * Created by uwoer on 2018/9/10.
  */
object ArrayTest {
  def main(args: Array[String]): Unit = {
    // 长度固定的数组
    val a = new Array[String](5)
    a(0) = "www"
    a(1) = "."
    a(2) = "aonemore"
    a(3) = "."
    a(4) = "com"

    val b = Array("www","aonemore","com")

    // 长度可变的集合
    val arrBuf = ArrayBuffer[Int]()
    // 使用+=操作符，添加一个元素或多个元素
    arrBuf += 1
    arrBuf += 3
    arrBuf += 5
    arrBuf += (2,4,6)
    // 使用trimEnd()函数，可以从尾部截断指定个数的元素
    arrBuf.trimEnd(2)

    // 使用++=操作符，添加其他集合中的所有元素
    arrBuf ++= Array(7, 8, 9)

    // Array与ArrayBuffer可以互相进行转换
    arrBuf.toArray
    arrBuf.toArray.toBuffer

    for (i <- 0 until b.length)
      println(b(i))

    // 跳跃遍历Array / ArrayBuffer 步长是2
    for(i <- 0 until (arrBuf.length, 2))
      println(arrBuf(i))

    // 从尾部遍历Array / ArrayBuffer
    for(i <- (0 until arrBuf.length).reverse)
      println(arrBuf(i))

    // 使用forEach 遍历Array / ArrayBuffer
    for (e <- b)
      println(e)

    // 求和
    println(arrBuf.sum)
    // 求最大值
    println(arrBuf.max)
    val sortArr = arrBuf.toArray
    // 排序
    Sorting.quickSort(sortArr)
    for (e <- sortArr)
      println(e)
    // 字符串拼装
    println(a.mkString)
    println(b.mkString("."))
    println(b.mkString("http://",".",""))



  }

}
