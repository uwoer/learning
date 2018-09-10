package com.uwola.scala

/**
  * Created by uwoer on 2018/9/10.
  */
object YieldTest {
  def main(args: Array[String]): Unit = {
    val a = Array(1, 3, 5, 7, 9)
    for (ele <- a) println(ele)
    println("===========")
    // yield 等价于 map
    val a2 = for (ele <- a) yield ele + 1
    for (ele <- a2) println(ele)
    println("===========")
    // a.map(x=>x+1).foreach(println(_))
    // a.map(_+1).foreach(println(_))
    a.map(_+1).foreach(println)
    println("===========")

    // if与yield组合使用等价于 filter map
    val a3 = for (ele <- a if ele<7) yield ele*ele
    for (ele <- a3) println(ele)
    println("===========")
    a.filter(_ < 7).map(x=> x*x).foreach(println)
  }

}
