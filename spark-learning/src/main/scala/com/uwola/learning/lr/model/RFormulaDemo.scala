package com.uwola.learning.lr.model

import org.apache.spark.ml.feature.RFormula
import org.apache.spark.sql.SparkSession

object RFormulaDemo {
  def main(args: Array[String]): Unit = {

    val spark=SparkSession.builder().appName(" ").master("local").getOrCreate()

    val dataset = spark.createDataFrame(Seq(
      (7,10, "US", "city1", 18, 1.0),
      (8,22, "CA", "city2", 12, 0.0),
      (9,100,"CA", "city2", 15, 0.0),
      (10,29,"CA", "city3", 15, 1.0),
      (11,88,"CA", "city3", 15, 0.0),
      (12,99,"CA", "city4", 2, 0.0)
    )).toDF("id", "count","country","city", "hour", "clicked")
/**
*country列6个不同取值时候占了五个维度  五个不同取值时候占了四个维度
*四个不同取值时候占了三个维度  三个不同取值占了两维度  两个不同取值占
*了一个维度，另外我们还操作了非StringType类型的hour 和 count列 因此*在country列所占维度基础上 再加上两个维度，就是所形成的新列features 
*该列值是一个向量  由上面组成的维度构成
*/
    val formula = new RFormula()
      .setFormula("clicked ~ country+city+hour+count")
      .setFeaturesCol("features")
      .setLabelCol("label")

    val output = formula.fit(dataset).transform(dataset)
    output.show(false)
    //output.write.json("spark-warehouse/Rformula")
//    output.select("features", "label").show(false)

  }
}