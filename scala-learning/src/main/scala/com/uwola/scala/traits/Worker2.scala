package com.uwola.scala.traits

/**
  * Created by uwoer on 2018/9/11.
  */
class Worker2 extends Handle3 with Handle2{
  /**
    * 重写
    */
  def work(work:String): Unit ={
    println("Worker2  我是 "+work)
    handle(work)
  }
}
