package com.uwola.scala

import com.uwola.scala.{Person, Upper, Upper2}

object App3 {

  def main(args: Array[String]): Unit = {
    println("hello world ...")
    var up = new Upper()
    println(up.upper("hello","world"))
    println(Upper2.upper("hello","world"))
    Upper2.upper2("hello","world","2")
    println()
    println(Upper2.upper3("hello","world","3"))
    println(Upper2.upper4("hello","world","4"))

    println(str("uwoer"))


    val stu = new Student3("",18,16.00);
    println(stu.age)
    println(stu.asInstanceOf[Person])
    println(stu.isInstanceOf[Student])
    println(stu.getClass == classOf[Person])
    println(stu.getClass == classOf[Student])

  }

  def str(str: String): String = {
    s"hello ${str} please login ..."
  }
}
