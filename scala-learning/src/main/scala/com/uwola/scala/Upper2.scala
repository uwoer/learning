package com.uwola.scala

/**
  * 单例
  */
object Upper2 {

  /**
    * 省去返回值类型的显示声明
    * 没有定义方法的返回值类型
    * Scala语言可以推断出返回值类型
    * 方法体最后一行的值的类型即为方法的返回值类型
    * ＝  表示方法体的起始位置
    * 推荐显示的声明方法的返回值的类型 便于bug调试
    * @param strings
    * @return
    */
  def upper(strings: String*) = strings.map(_.toUpperCase())

  def upper2(strings: String*) = strings.map(_.toUpperCase()).foreach(printf("%s ",_))

  def upper3(strings: String*) = strings.map(_.toUpperCase()).mkString(" ")

  def upper4(strings: String*) = strings.map(_.toUpperCase()).mkString("===>"," ","<====")


}
