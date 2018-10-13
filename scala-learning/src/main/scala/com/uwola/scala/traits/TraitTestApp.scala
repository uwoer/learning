package com.uwola.scala.traits

/**
  * Created by uwoer on 2018/9/11.
  */
object TraitTestApp {
  def main(args: Array[String]): Unit = {
    val stu = new Student()
    stu.name = "uwoerla"
    stu.age = 31
    stu.sayHello()
    stu.sleep()

    // 未混入trait的Teacher
    val t1 = new Teacher()
    t1.teach("数学")

    //  通过with关键字为实例混入trait
    //  trait field的初始化  提前定义  （Scala高级特性)
    val t2 = new {var name = "怪老头儿"} with Teacher with Person1
    println(t2.name)
    t2.sayHello()
    t2.sleep()
    t2.teach("英语")



    // trait继承class  此时这个class就会成为所有继承该trait的类的父类
    val stu2 = new Student2
    val tc2 = new Teacher2
    stu2.idNo = "1991....."
    stu2.eat()
    tc2.idNo = "1980....."
    tc2.eat()


    val w1 = new Worker1()
    w1.work("生产工")
    val w2= new Worker2()
    w2.work("包装工")
    val w3= new Worker3()
    w3.work("全能工")
  }

}
