package com.uwola.learning.streaming

import com.alibaba.fastjson.JSON

object JsonTest {
  def main(args: Array[String]): Unit = {
    val s = """{"user_id": 795, "hour": 19, "order_id": 2322381, "eval_set": "prior", "order_dow": 2, "order_number": 3, "day": 16.0}"""
    val mess = JSON.parseObject(s, classOf[Orders])
    println(mess.order_id,mess.order_dow,mess.order_hour_of_day)
    val predicates =
      Array(
        "2015-09-16" -> "2015-09-30",
        "2015-10-01" -> "2015-10-15",
        "2015-10-16" -> "2015-10-31",
        "2015-11-01" -> "2015-11-14",
        "2015-11-15" -> "2015-11-30",
        "2015-12-01" -> "2015-12-15"
      ).map {
        case (start, end) =>
          s"cast(modified_time as date) >= date '$start' " + s"AND cast(modified_time as date) <= date '$end'"
      }
    predicates.foreach(println)

    val predicates2 =
    Array(
      ("2015-09-16","2015-09-30"),
      ("2015-10-01" , "2015-10-15"),
      ("2015-10-16" , "2015-10-31"),
      ("2015-11-01" , "2015-11-14"),
      ("2015-11-15" , "2015-11-30"),
      ("2015-12-01" , "2015-12-15")
    ).map {
      case (start, end) =>
        s"cast(modified_time as date) >= date '$start' " + s"AND cast(modified_time as date) <= date '$end'"
    }
    println("==============================================")
    println("==============================================")
    println("==============================================")
    predicates2.foreach(println)
  }

}
