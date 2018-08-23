package com.uwola.scala

object Test1 {

  def main(args: Array[String]): Unit = {
    //链式处理
    List(1,2,3,4).filter((i: Int) => isEven(i)).foreach((i: Int) => println(i))
    List(1,2,3,4).filter(i => isEven(i)).foreach(i => println(i))
    List(1,2,3,4).filter(isEven).foreach(println)
    List(1,2,3,4) filter isEven foreach println
    println("hello".length)
    println("hello".length())


  }
  def isEven(n: Int) = (n % 2) == 0

}
