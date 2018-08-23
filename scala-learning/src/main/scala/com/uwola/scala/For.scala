package com.uwola

object For {

  def main(args: Array[String]): Unit = {
    //生成器表达式
    for(i <- 1 to 10) println(i)
    println("===================================")
    val list = List("hello","my","dear","girl","and","boy")
    for(word <- list) println(word)
    println("===================================")
    //塞选元素
    for(i <- 1 to 10 if(isEven(i))) println(i)
    println("===================================")
    for(word <- list if (word.contains("e"))) println(word)
    println("=============for yield===============")
    val list2 = for(word <- list if (word.contains("e"))) yield word
    list2 foreach println
    println("=============for yield===============")
    val list3 = for{
      word <- list
      if (word.contains("e"))
      if (word.contains("a"))
    //由于程序的链式执行  以上两个if 等价于  if (word.contains("e")) && if (word.contains("a"))
    } yield word
    list3 foreach println
    println("=============for yield===============")
    val list4 = for{
      word <- list
      if (word.contains("e") || word.contains("a"))
    } yield word
    list4 foreach println

  }
  def isEven(n: Int) = (n % 2) == 0

}
