package com.uwola.scala

/**
 * @author ${user.name}
 */
object App2 extends App{
  if (args.length > 0) println("hello, " + args(0))
  else println("Hello World!!!")
  println(Season.SPRING)
  println(Season2(0))
  println(Season2.withName("spring"))
  for (ele <- Season2.values) println(ele)
}
