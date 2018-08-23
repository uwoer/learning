package com.uwola.cases

object CaseApp {
  def main(args: Array[String]): Unit = {
    var p1 = new Point();
    println(p1)
    var p2 = new Point(2);
    println(p2)
    var p2b = new Point(2);
    println(p2b)
    println(p2b==p2)
    println(p2b.equals(p2))
    //未显示制定参数时参数按从左到右的顺序赋值
    var p3 = new Point(2,2);
    println(p3)
//    显示指定给某个参数赋值
    var p4 = new Point(y=2);
    println(p4)
    println(p4.x)
    println(p4.y)


  }

}
