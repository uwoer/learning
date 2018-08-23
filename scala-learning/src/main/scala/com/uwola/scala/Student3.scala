package com.uwola.scala

import com.uwola.scala.Person

class Student3(name: String, age: Int, var score: Double) extends Person(name,age) {
  def this(name: String) {
    this(name, 0, 0)
  }
  def this(age: Int) {
    this("leo", age, 0)
  }
}

