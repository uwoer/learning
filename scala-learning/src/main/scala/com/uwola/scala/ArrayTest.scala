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

    val list = List(1)
    println(list.head)
    //emety list
    println(list.tail == Nil)
    println(list.tail == None)
    println(list.tail == null)

    val list2 = 0 :: list
    println(list2.head)
    println(list2.tail)
    val list3 = -1 :: list2
    println(list3.head)
    println(list3.tail)

    val list4 =list3.::(3)
    println(list4.head)
    println(list4.tail)

    val linkedList = scala.collection.mutable.LinkedList(1, 2, 3, 4)
    println(linkedList.elem)
    println(linkedList.next)

    // 没有重复元素无序的集合
    val set = Set(1, 2, 3)
    set + 4
    val hashSet = new scala.collection.mutable.HashSet[Int]()
    hashSet += 1; hashSet += 2; hashSet += 3
    // 会用一个链表维护插入顺序
    val linkedHashSet = new scala.collection.mutable.LinkedHashSet[Int]()
    linkedHashSet += 1; linkedHashSet += 2; linkedHashSet += 3

    // SrotedSet会自动根据key来进行排序
    val sortedSet = scala.collection.mutable.SortedSet("3", "1", "2")

    //循环打散
    List("In order to be irreplaceable, one must always be different.", "Be just to all, but trust not all. ").flatMap(_.split(" ")).foreach(println)


    Array.range(1,10)
    List.range(1,10)
    Array.range(1,10)
//    val a = (1 to 10).toArray
//    val l = (1 to 10) by 2 toList
//    val l = (1 to 10).by(2).toList
  }

}
