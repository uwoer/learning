package com.uwola.scala.traits

/**
  * Created by uwoer on 2018/9/11.
  */
class Teacher2 extends Person2{

  def teach(course:String): Unit ={
    println("I teach "+course)
  }
  override var name: String = _
}
