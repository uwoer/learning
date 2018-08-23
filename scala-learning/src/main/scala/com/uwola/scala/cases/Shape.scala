package com.uwola.cases

/**
  * Unit 即为java的 null
  *  s"draw: ${this.toString}" 插值字符串  相当与定义了一个模版
  */
abstract class Shape() {
  def draw(f: String => Unit): Unit = f(s"draw: ${this.toString}")

}
