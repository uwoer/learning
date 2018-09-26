package com.uwola.scala.traits

/**
  * 继承class的trait
  * Created by uwoer on 2018/9/11.
  */
trait Person2 extends IdCard{
  // 具体方法
  var name:String
  // 具体属性
  var age = 1
  /**
    * 吃饭
    * 具体方法
    */
  def eat(): Unit ={

  }
  /**
    * 睡觉
    * 具体方法
    */
  def sleep(): Unit ={
    println("美好的一天又开始了...")
  }
  /**
    * 打豆豆
    * 具体方法
    */
  def amuse(): Unit ={

  }
  /**
    * 问候
    * 抽象属性调用抽象方法
    */
  def sayHello(): Unit ={
    println("hello my name is "+name+" my age is "+age+", nice to meet you !!!")
  }
}
