package com.uwola.scala.clazz

import com.alibaba.fastjson.JSON

/**
  * Created by uwoer on 2018/9/10.
  */
object App {
  def main(args: Array[String]): Unit = {
    val p = new Person()
    // setter
    p.name = "uwoerla"
    // setter方法
    p.age = 30
    // 通过getter方法打印
    println("p name is "+p.name+" age is "+p.age )

    val p1 = new Person1(age = 31,name = "uwoerla")
    println("p1 name is "+p1.name+" age is "+p1.age )


    val p2 = new Person2()
    // setter
    p2.name = "uwoerla"
    // setter方法
    p2.age = 30
    // 通过getter方法打印
    println("p2 name is "+p2.name+" age is "+p2.age )
    // setter
    p2.setName("aonemore")
    // setter方法
    p2.setAge(29)
    // 通过getter方法打印
    println("p2 name is "+p2.getName+" age is "+p2.getAge )
    val jsonStr = JSON.toJSON(p2).toString
    println(jsonStr)
    val json2p = JSON.parseObject(jsonStr,classOf[Person])
    println("json2p name is "+json2p.name+" age is "+json2p.age )
    var json2p1 = JSON.parseObject(jsonStr,classOf[Person1])
    println("json2p1 name is "+json2p1.name+" age is "+json2p1.age )
    val json2p2 = JSON.parseObject(jsonStr,classOf[Person2])
    println("json2p2 name is "+json2p2.name+" age is "+json2p2.age )
    println("json2p2 name is "+json2p2.getName+" age is "+json2p2.getAge )

    val json = "{\"name\":\"uwoer\"}"
    json2p1 = JSON.parseObject(json,classOf[Person1])
    println("json2p1 name is "+json2p1.name+" age is "+json2p1.age )
  }

}
