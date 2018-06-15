package com.uwola.learning.streaming

import org.apache.spark.sql.types.{IntegerType, StringType, StructField, StructType}


object Schema {
  /*val logSchema:StructType = (columns:Array[String]) =>
    StructType(columns.map(x=>StructField(x,StringType,true)))*/

  val structType = StructType(Array(
    StructField("id",IntegerType,true),
    StructField("name",StringType,true),
    StructField("age",IntegerType,true)
  ))


}
