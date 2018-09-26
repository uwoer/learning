package com.uwola.scala.traits

/**
  * Created by uwoer on 2018/9/11.
  */
trait Handle1 extends Handle{
  /**
    * 重写
    */
  override def handle(work:String): Unit ={
    println("完成工序 A ")
    super.handle(work)
  }
}
