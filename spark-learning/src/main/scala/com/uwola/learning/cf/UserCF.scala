package com.uwola.learning.cf

import breeze.numerics.{pow, sqrt}
import org.apache.spark.sql.{DataFrame, Row}
import org.apache.spark.sql.functions._

object UserCF {
  def userSim(df:DataFrame): DataFrame ={
    import df.sparkSession.implicits._
//    计算每个用户对应打分平方和开根号的分母
    val userScoreSum = df.rdd.map(x=>(x(0).toString,x(2).toString))
      //groupByKey() 后  得到每个用户的打分集合
      .groupByKey()
//      .mapValues(x=>sqrt(x.toArray.map(r=>pow(r.toDouble,2)).sum))
      .mapValues(x=>sqrt(x.map(r=>pow(r.toDouble,2)).sum))
      .toDF("user_id","rating_sqrt_sum")
//    需要计算用户与用户之间的相似度，所以需要两个用户之间，所以copy一个dataFrame，列名做了标识
    val df_t = df.selectExpr("user_id as user_id1", "item_id as item_id1","rating as rating1")

    //   同一个item底下的用户进行两两组合
    val df_decare = df.join(df_t,df("item_id")===df_t("item_id1"))
      .filter("cast(user_id as long) < cast(user_id1 as long)")

    val df_product = df_decare.selectExpr("user_id","user_id1","rating*rating1 as rating_product")
    // (rating*rating1)/sqrt(pow(rating)+pow(rating1))
    val df_sim_group = df_product.groupBy("user_id","user_id1")
      .agg("rating_product"->"sum")
      .withColumnRenamed("sum(rating_product)","rating_sum_pro")

    val userScoreSum1 = userScoreSum.selectExpr("user_id as user_id1",
      "rating_sqrt_sum as rating_sqrt_sum1")

    val df_sim = df_sim_group.join(userScoreSum,"user_id")
      .join(userScoreSum1,"user_id1")
      .selectExpr("user_id","user_id1","rating_sum_pro/(rating_sqrt_sum1*rating_sqrt_sum*1.0) as user_sim")


    df_sim
  }


  /**
    * 获取用户的推荐列表
    * @param df
    * @param df_sim
    * @param k       前TopN相似的用户
    * @param m       前TopN比分推荐商品
    * @return
    */

  def simUserItem(df:DataFrame,df_sim:DataFrame,k:Int,m:Int): DataFrame ={
    import df.sparkSession.implicits._
    /**topN个相似用户
      * */
    val df_nsim = df_sim
      .rdd.map(x=>(x(0).toString,(x(1).toString,x(2).toString)))
      // 按user_id聚合
      .groupByKey()
      .mapValues(
        _.toArray.sortWith(_._2.toDouble>_._2.toDouble
        ).slice(0,k)
      )
      // 按user_id打散
      .flatMapValues(x=>x)
      .toDF("user_id","arr_sim_users")
      .selectExpr("user_id","arr_sim_users._1 as sim_user","arr_sim_users._2 as sim_score")


//    val df = spark.sql("select user_id,item_id,rating from badou.udata")

    /**用户的历史打分item集合
      * */
    val userItemsRated_u = df
      .rdd.map(x=>(x(0).toString,(x(1).toString,x(2).toString)))
      .groupByKey()
      .mapValues(_.toArray.map(x=>x._1+"_"+x._2.toString).mkString(","))
      .toDF("user_id","items_rated")

    /**udf1：用户相似度分值 * 该相似用户的物品打分
      * */
    val simProdRatingUDF = udf{(sim_score:Double,items_rated:String)=>
      items_rated.split(",")
        .map(x=>(x.split("_")(0),x.split("_")(1).toDouble*sim_score))
        .map(x=>x._1+"_"+x._2)
        .mkString(",")
    }
    /**udf2：
      * 1. 用户已经打分的电影做过滤
      * 2. 对不同相似用户的历史打分电影集合做去重（sum，avg，max）
      * 这里用的是sum
      * */
    val filterItemRatedUDF = udf{(items_rated_prod:String,items_rated:String)=>
      val filterMap = items_rated.split(",")
        .map(x=>(x.split("_")(0),x.split("_")(1))).toMap

      items_rated_prod.split(",")
        .map(x=>(x.split("_")(0),x.split("_")(1).toDouble))
        .filter(x=>filterMap.getOrElse(x._1,-1)== -1)
        .groupBy(_._1)
        .mapValues(_.map(_._2).sum)
        .toArray
        .sortWith(_._2>_._2)
        .slice(0,10)
        .mkString(",")
    }

    // 先加权计算 再过滤已评论电影
    df_nsim.join(userItemsRated_u,df_nsim("sim_user")===userItemsRated_u("user_id"))
      .drop(userItemsRated_u("user_id"))
      .withColumn("items_rated_prod",simProdRatingUDF(col("sim_score"),col("items_rated")))
      .select("user_id","items_rated_prod")
      .rdd.map(x=>(x(0).toString,x(1).toString))
      .groupByKey()
//      .mapValues(_.toArray.mkString(","))
      .mapValues(_.mkString(","))
      .toDF("user_id","items_rated_prod")
      .join(userItemsRated_u,"user_id")
      .withColumn("finally_items_rating",filterItemRatedUDF(col("items_rated_prod"),col("items_rated")))
      .select("user_id","finally_items_rating")



    //  先过滤已评论电影再 加权计算（不太好处理 过程比较绕）


  }


  def userResemble(df_sim:DataFrame,k:Int): DataFrame ={
    import df_sim.sparkSession.implicits._

    df_sim.selectExpr("user_id as resembleUid","user_sim").where(s"user_id1 = ${userId}").
      union(df_sim.selectExpr("user_id1 AS resembleUid","user_sim").where(s"user_id = ${userId}")).
      orderBy($"user_sim".desc).withColumnRenamed("resembleUid","user_id").limit(k)

  }


  /**
    * topN 个相似用户
    * @param userId
    * @param df_sim
    * @param k
    * @return
    */
  def userResemble(userId:Int,df_sim:DataFrame,k:Int): DataFrame ={
    import df_sim.sparkSession.implicits._

    df_sim.selectExpr("user_id as resembleUid","user_sim").where(s"user_id1 = ${userId}").
      union(df_sim.selectExpr("user_id1 AS resembleUid","user_sim").where(s"user_id = ${userId}")).
      orderBy($"user_sim".desc).withColumnRenamed("resembleUid","user_id").limit(k)

  }

  def itemResemble(userId:Int,df:DataFrame,df_userResemble:DataFrame,k:Int): DataFrame ={
    import df.sparkSession.implicits._
    //需要考虑下如何去除用户已经评论过的item
    val df_have = df.selectExpr("user_id","item_id").where(s"user_id = ${userId}")
    //得到相似用户的item加权评分的倒排
    val itemResemble_df = df.selectExpr("user_id","item_id","rating").join(df_userResemble,"user_id").selectExpr("item_id","rating*user_sim").withColumnRenamed("(rating * user_sim)","rating_resemble").orderBy($"rating_resemble".desc)

//    itemResemble_df.rdd.foreachPartition(p=>{
//      p.filter(x:(Row) => {x(0)>1})
//    })
    null
  }
}
