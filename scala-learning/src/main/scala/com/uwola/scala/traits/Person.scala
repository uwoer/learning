package com.uwola.scala.traits

/**
  * Created by uwoer on 2018/9/11.
  */
trait Person {
  // 抽象属性
  var name:String
  // 具体属性
  var age = 1
  /**
    * 吃饭
    * 抽象方法
    */
  def eat()
  /**
    * 睡觉
    * 具体方法
    */
  def sleep(): Unit ={
    println("美好的一天又开始了...")
  }
  /**
    * 打豆豆
    * 抽象方法
    */
  def amuse()
  /**
    * 问候
    * 抽象属性调用抽象方法
    */
  def sayHello(): Unit ={
    println("hello my name is "+name+" my age is "+age+", nice to meet you !!!")
  }
}
