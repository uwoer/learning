package com.uwola.scala

object HelloImpl extends Hello("hello") {
  override def sayHello(name: String) = {
    println(message + ", " + name)
  }
}