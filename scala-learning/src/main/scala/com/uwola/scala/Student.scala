package com.uwola.scala

/**
  * Created by uwoer on 2018/4/28.
  */
class Student {
  //此字段有public的get set方法
  var name = "leo"
  //此字段有private的get set方法
  private var age = "leo"
  //此字段仅有public的get方法
  val name_val = "name_val"
  //此字段无get set方法
  private[this] var name_private_this = "name_private_this"
  //此字段无get set方法
  private[this] val name_private_this_val = "name_private_this_val"

  def setName_private_this_=(name: String){
    name_private_this = name
//    name_private_this_val = name
    println(name)
    println(name_private_this)
  }



}
