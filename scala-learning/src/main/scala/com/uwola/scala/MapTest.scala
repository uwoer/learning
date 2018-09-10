package com.uwola.scala

import scala.collection.immutable.HashMap

/**
  * Created by uwoer on 2018/9/10.
  */
object MapTest {
  def main(args: Array[String]): Unit = {
    // 不可变的Map  scala.collection.immutable.Map
    //    val tiobeRank = Map(("Java",1), ("Python",3),("Scala",27))
    val tiobeRank = Map("Java" -> 1, "Python" -> 3, "Scala" -> 27)
    // 编译失败
    // tiobeRank("C") = 2

    // 不可变的HashMap
    val tiobeRank2 = HashMap("Java" -> 1, "Python" -> 3, "Scala" -> 27)
    // 编译失败
    // tiobeRank2("C") = 2


    // 可变的map
    val redmonkRank = scala.collection.mutable.Map("Java" -> 2, "Python" -> 3, "Scala" -> 12)
    redmonkRank("C") = 5

    // 可变的HashMap
    val hashMap = scala.collection.mutable.HashMap[String, Int]()
    hashMap.put("Java", 2)
    // 无则增加 有则更新Python键对应的值
    hashMap("Python") = 3
    /*
    Exception in thread "main" java.util.NoSuchElementException: key not found: Scala
      at scala.collection.MapLike$class.default(MapLike.scala:228)
      at scala.collection.AbstractMap.default(Map.scala:59)
      at scala.collection.mutable.HashMap.apply(HashMap.scala:65)
      at com.uwola.scala.MapTest$.main(MapTest.scala:31)
      at com.uwola.scala.MapTest.main(MapTest.scala)
     */
    //    hashMap("Scala")
    // true
    println(hashMap.get("Scala") == None)
    // true
    println("".equals(hashMap.getOrElse("Scala", "")))

    // 新增键值对
    redmonkRank += ("JavaScript" -> 1, "C" -> 9)
    // 删除某个键值对
    redmonkRank -= ("C")

    //打印KV
    for ((k, v) <- redmonkRank) println(k + " " + v)
    println("===========")
    //打印K
    for (k <- redmonkRank.keys) println(k)
    //    for(k <- redmonkRank.keySet) println(k)
    println("===========")
    //只打印值
    for (v <- redmonkRank.values) println(v)
    //    for(v <- redmonkRank.valuesIterator) println(v)
    println("===========")
    //反转KV
    val redmonkRank_reverse = for ((k, v) <- redmonkRank) yield (v, k)
    for ((k, v) <- redmonkRank_reverse) println(k + " " + v)
    println("===========")

    // SortedMap可以自动对Map的key的排序
    val rank = scala.collection.immutable.SortedMap("Scala" -> 27, "Java" -> 1, "Python" -> 3)
    for ((k, v) <- rank) println(k + " " + v)
    println("===========")

    // LinkedHashMap可以记住插入entry的顺序
    val rank_linkedHashMap = new scala.collection.mutable.LinkedHashMap[String, Int]
    rank_linkedHashMap += ("Scala" -> 27)
    rank_linkedHashMap += ("Java" -> 1)
    rank_linkedHashMap += ("Python" -> 3)
    for ((k, v) <- rank_linkedHashMap) println(k + " " + v)

  }
}
