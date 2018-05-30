package com.uwola.scala

import scala.collection.mutable.ArrayBuffer

/**
 * @author ${user.name}
 */
object App {
  
  def foo(x : Array[String]) = x.foldLeft("")((a,b) => a + b)

  def canLaunchExecutor(pos: Int): Boolean = {
    System.out.println("=====>"+pos)
    System.out.println(pos==0)
    pos==0
  }
  
  def main(args : Array[String]) {
    println( "Hello World!" )
    println("concat arguments = " + foo(args))

    val a = ArrayBuffer(1,2,3,4,5)
    println(a.max)
    println(a.sum)
    println(a.mkString("("," String, "," String)"))
    a.remove(0,2)
    println(a.toString())
    val b = a.toArray
    println(b)


    val ages = scala.collection.mutable.Map[String,Int]()
    ages("uweor") = 33
    println(ages)
    ages +=("dudu"->1)
    println(ages)


    val s = new Student();
    println(s.name)
    s.name="xxxx"
    println(s.name)

    println(s.name_val)
//    s.name_val="xxxx"

    println(s)
    s.name="xxxx"
    println(s.name)

    s.setName_private_this_=("dddddd")

    val s2 = new Student2();
    s2.getName
    s2.setName("hhhhhhhhhh")

    HelloImpl.sayHello("dudu")

    a.filter(canLaunchExecutor)
  }

}
