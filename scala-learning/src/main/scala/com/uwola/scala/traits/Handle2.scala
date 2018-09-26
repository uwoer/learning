package com.uwola.scala.traits

/**
  * Created by uwoer on 2018/9/11.
  */
trait Handle2 extends Handle{
  override def handle(work:String): Unit ={
    println("完成工序 B ")
    super.handle(work)
  }
}
