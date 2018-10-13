package com.uwola.scala.traits

/**
  * Created by uwoer on 2018/9/11.
  */
class Worker1 extends Handle2 with Handle1{
  def work(work:String): Unit ={
    println("Worker1 我是 "+work)
    handle(work)
  }
}
