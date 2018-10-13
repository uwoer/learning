package com.uwola.scala.traits

/**
  * Created by uwoer on 2018/9/11.
  */
trait Handle3 extends Handle{
  override def handle(work:String): Unit ={
    println("完成工序 C ")
    super.handle(work)
  }
}
