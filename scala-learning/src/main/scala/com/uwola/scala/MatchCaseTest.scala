package com.uwola.scala

import java.io.{FileNotFoundException, IOException}

/**
  * Created by uwoer on 2018/9/13.
  */
object MatchCaseTest {
  def main(args: Array[String]): Unit = {
    salary("P1", 3)
  }

  def salary(level: String, years: Int) {
    level match {
      case "P5" => println("P5=====> 18W")
      case "P6" => println("P6=====> 30W")
      case "P7" => println("P7=====> 40W")
      case "P8" => println("P8=====> 70W")
      case "P9" => println("P9=====> 90W")
      case _ => println("years ===>" + years + " your level is " + level)

      // 模式匹配中进行变量赋值
      case _level if years > 10 => println("years ===>" + years + " your level is " + _level)
      case _level => println("==================>" + _level)
    }
  }

  def processException(e: Exception) {
    e match {
      case e1: IllegalArgumentException => println("IllegalArgumentException: " + e1)
      case e2: FileNotFoundException => println("FileNotFoundException: " + e2)
      case e3: IOException => println("IOException: " + e3)
      case _: Exception => println("cannot know which exception you have!" )
    }
  }


}
