package com.uwola.scala.traits

/**
  * Created by uwoer on 2018/9/11.
  */
class Worker3 extends Handle3 with Handle2 with Handle1{
  /**
    * 重写
    */
  def work(work:String): Unit ={
    println("Worker3  我是 "+work)
    handle(work)
  }
}
