package com.uwola.learning.cf

import org.apache.spark.sql.SparkSession

object MainLanch {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().master("local[2]").enableHiveSupport().getOrCreate()
    val df = spark.sql("select user_id,item_id,rating from badou.udata")
    df.show(false)
    val df_sim = UserCF.userSim(df)
    UserCF.simUserItem(df,df_sim,10,10)
  }

}
