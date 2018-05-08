package com.uwola.learning

import org.apache.spark.sql.SparkSession
import org.apache.spark.{SparkConf, SparkContext}

class JobTask {
  /**
    * wordcount ，然后打印出前20个词频最大的
    * @param sc
    */
  def wordCount(sc:SparkContext): Unit = {
    val textRdd = sc.textFile("C:\\Users\\MACHENIKE\\Desktop\\hadoop2.0\\mapreduce_wordcount_python\\The_Man_of_Property.txt")
    textRdd.flatMap(line => line.split(" ")).map(line => (line, 1)).reduceByKey(_ + _).sortBy(_._2, ascending = false).take(20).foreach(println)
  }


  /**
    * product 统计/特征（3个特征）
    * 1. 统计product被购买的数据量
      2. 统计product 被reordered的数量（再次购买）
      3. 结合上面数量统计product购买的reordered 的比率
    */
  def getProductFeature(sparkSession:SparkSession):Unit= {
    val sql = "select product_id,count(*) as product_count,sum(reordered) as reordered_count,(sum(reordered)/count(*)) as ratio from trains group by product_id limit 10"
    val df = sparkSession.sql(sql)
    df.show()
  }
    /**
      * user 统计/特征 （）
        1. 每个用户平均购买订单的间隔周期
        2. 每个用户的总订单数量
        3. 每个用户购买的product商品去重后的集合数据
        4. 每个用户总商品数量以及去重后的商品数量
        5. 每个用户购买的平均每个订单的商品数量（hive已做过）
       */
    def getUserFeature(sparkSession: SparkSession):Unit={
      val sql ="select * from\n(\nselect ord.user_id,(sum(days_since_prior_order)+sum(cast(order_hour_of_day as double))/24)/count(1) ,\nsum(ord.order_number),collect_list(distinct pri.product_id),\nsize(collect_list(pri.product_id)),size(collect_list(distinct pri.product_id))\nfrom orders ord  \njoin (select * from priors  limit 100)pri \nwhere   ord.order_id = pri.order_id\ngroup by user_id \n) a\njoin \n(\nselect ord.user_id,avg(pri.products_cnt) as avg_prod\nfrom \n(select order_id,user_id from orders)ord \njoin \n(select order_id,count(1) as products_cnt from priors  group by order_id )pri \non ord.order_id=pri.order_id\ngroup by ord.user_id\n) b\non a.user_id = b.user_id\nlimit 10"
      val df = sparkSession.sql(sql)
      df.show()
    }
}

object test{
  def main(args: Array[String]): Unit = {
    val sparkConf = new SparkConf().setMaster("local").setAppName("test")
    val sc = new SparkContext(sparkConf)
    val sparkSession = SparkSession.builder.master("local").appName("spark session example").enableHiveSupport().getOrCreate()

    sparkSession.sql("show databases").show()
//    val job = new JobTask()
//    job.wordCount(sc) //第一题
//    job.getProductFeature(sparkSession) //第二题
//    job.getUserFeature(sparkSession) //第三题

  }

  def zuoye1(spark: SparkSession):Unit={
    import spark.sql
    val products = sql("select * from badou.products")
    products.show
    val priors = sql("select * from uwola.priors")
    priors.groupBy("product_id").count().show

  }

  def zuoye2(spark: SparkSession):Unit={
    import spark.sql
    val orders = sql("select * from uwola.products")
    orders.show(5)
//    scala 命令行中可以执行
//    orders.filter(col("eval_set") === "test").show(5)

    val products = sql("select * from uwola.products")
    products.selectExpr().show(5)

    val priors = sql("select * from uwola.priors")
//    priors.groupBy("product_id").count().show
    val productCnt = priors.groupBy("product_id").count()

    //缓存（当前进程有效）
    val cache = priors.groupBy("product_id").count().cache()
    cache.show(5)
    //清除缓存
    cache.unpersist()

//    priors.selectExpr("product_id","cast(reordered as int)").filter(col("reordered")===1).groupBy("product_id").count().show(5)
//    priors.selectExpr("product_id","cast(reordered as int)").groupBy("product_id").sum("reordered")
//    2.统计product 被reordered的数量（再次购买）（这商品是不是消耗品） product_id做group by(聚合)，统计一下sum（reorder）的值
//    agg一般是搭配groupby这种聚合函数使用  和单独使用sum的差别，是在一次聚合中可以统计出来多个值
//    val productSumReorder = priors.groupBy("product_id").agg(sum("reordered"),avg("reordered")).show(5)
//    列重命名
//    val productSumReorder = priors.groupBy("product_id").agg(sum("reordered"),avg("reordered")).withColumnRenamed("sum(reordered)","sum_re").withColumnRenamed("avg(reordered)","avg_re").show(5)

//    val jproduct = productCnt.join(productSumRecorder,"product_id")
    //    udf：
    import org.apache.spark.sql.functions._
    val avg_udf = udf((sm:Long,cnt:Long)=>sm.toDouble/cnt.toDouble)
//    udf的使用：
//    jproduct.withColumn("mean_re",avg_udf(col("sum_re"),col("count"))).show(5)
//    jproduct.selectExpr("*","sum_re/count as mean_re")






//    1.每个用户平均购买订单的间隔周期

//    1)、用户的第一个订单没有间隔天数的，需要赋值为0

//    注意这里drop 函数的使用点
//    val ordersNew =orders.selectExpr("*","if(days_since_prior_order='',0,days_since_prior_order) as dspo").drop("days_since_prior_order").show()
//    val ordersNew =orders.selectExpr("*","if(days_since_prior_order is null,0,days_since_prior_order) as dspo").drop("days_since_prior_order").show()
    val ordersNew =orders.selectExpr("*","if(days_since_prior_order is null,0,days_since_prior_order) as dspo").drop("days_since_prior_order")

    ordersNew.selectExpr("user_id","dspo").groupBy("user_id").avg("dspo").show()

//    2.每个用户的总订单数量
    orders.groupBy("user_id").count().show()

//    3.每个用户购买的product商品去重后的集合数据
//    val op = orders.join(priors,"order_id").select("user_id","product_id").show(5)
    val op = orders.join(priors,"order_id").select("user_id","product_id")

//    DataFrame转RDD处理：
    val rddRecords = op.rdd.map(x=>(x(0).toString,x(1).toString)).groupByKey().mapValues(_.toSet.mkString(","))

//    RDD转DataFrame：
//    需要隐式转换：
    import spark.implicits._
    rddRecords.toDF("user_id","product_records")

//    4.每个用户总商品数量以及去重后的商品数量
//    总商品数量:
      orders.join(priors,"order_id").groupBy("user_id").count().show(5)

//    同时取到product去重的集合和集合的大小
    val rddRecords2=op.rdd.map(x=>(x(0).toString,x(1).toString))
      .groupByKey()
      .mapValues{record=>
        val rs = record.toSet
        (rs.size,rs.mkString(","))
      }.toDF("user_id","tuple")
      .selectExpr("user_id","tuple._1 as prod_dist_cnt","tuple._2 as prod_records")



//    5.每个用户购买的平均每个订单的商品数量

//    1）先求每个订单的商品数量【对订单做聚合count（）】
    val ordProCnt = priors.groupBy("order_id").count()

//    2）求每个用户订单中商品个数的平均值【对user做聚合，avg（商品个数）】
    orders.join(ordProCnt,"order_id").groupBy("user_id").avg("count")





  }


}