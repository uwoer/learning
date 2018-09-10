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





    for(i <- 1 to 9; j <- 1 to 9) {
      if(j == 9) {
        println(i * j)
      } else {
        print(i * j + " ")
      }
    }
    println("++++++++++++++++++++++++++")
    for(i <- 1 to 9; j <- 1 to 9) {
      if(j == 9) {
        println(if((i * j).toString.length<2){" "+i * j}else{i * j})
      } else {
        print(if((i * j).toString.length<2){" "+i * j+ " "}else{i * j + " "})
      }
    }


    var n = 10
    for(i <- 1 to n) println(i)
    for(i <- 1 until n) println(i)

    import scala.util.control.Breaks._
    breakable {
      while(n > 0) {
        if(n<6) break;
        println(n)
        n -= 1
      }
    }


    sayHello("uwoerla")
    sayHello("uwoerla",25)

    sayHello(name = "uwoerla")
    sayHello(name = "uwoerla",age = 25)
    sayHello(age = 25,name = "uwoerla")

    println(sum(1, 2, 3, 4, 5))
    println(sum(1 to 5: _*))

    //    for(c <- "hello world") println(c)
//    for(i <- 1 to 10) yield i



  }


  def sayHello(name: String, age: Int = 20) {
    println("Hello, " + name + ", your age is " + age)
  }

  def sum(nums: Int*) = {
    var res = 0
    for (num <- nums) res += num
    res
  }

}
