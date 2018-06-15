package com.uwola.learning.streaming

import com.alibaba.fastjson.JSON

object JsonTest {
  def main(args: Array[String]): Unit = {
    val s = """{"user_id": 795, "hour": 19, "order_id": 2322381, "eval_set": "prior", "order_dow": 2, "order_number": 3, "day": 16.0}"""
    val mess = JSON.parseObject(s, classOf[Orders])
    println(mess.order_id,mess.order_dow,mess.order_hour_of_day)
  }

}
