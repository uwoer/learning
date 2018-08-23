package com.uwola.scala

class Upper {

  def upper(strings:String*): Seq[String] = {
    strings.map((s:String) => s.toUpperCase())
  }


}
