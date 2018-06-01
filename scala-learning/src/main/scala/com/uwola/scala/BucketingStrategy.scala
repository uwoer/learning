package com.uwola.scala

// Java
import java.text.SimpleDateFormat
import java.util.{Date, TimeZone}

/**
  * Object uses downsampling method to create metadata from each
  * EventType log record. Parsing the ISO 8601
  * datetime stamp to the minute means downsampling aka reducing
  * precision.
  *
  * Bucketing
  * A family of aggregations that build buckets, where each bucket
  * is associated with a key and an EventType criterion. When the
  * aggregation is executed, all the buckets criteria are evaluated
  * on every EventType in the context and when a criterion matches,
  * the EventType is considered to "fall in" the relevant bucket.
  * By the end of the aggregation process, we’ll end up with a
  * list of buckets - each one with a set of EventTypes that
  * "belong" to it.
  *
  */
object BucketingStrategy {

  private val BucketToMinuteFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:00.000")
  BucketToMinuteFormatter.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"))
  private val BucketToHourFormatter = new SimpleDateFormat("yyyy-MM-dd HH:00:00")
  BucketToHourFormatter.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"))
  private val BucketToDayFormatter = new SimpleDateFormat("yyyy-MM-dd")
  BucketToDayFormatter.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"))

  /**
    * Function to bucket a date based on
    * our bucketing strategy. Bucketing
    * means downsampling aka reducing
    * precision.
    *
    * @param date The Java Date to bucket
    * @return the downsampled date in String
    *         format
    */
  def bucket(date: Date): String = BucketToMinuteFormatter.format(date)
  def bucket_hour(date: Date): String = BucketToHourFormatter.format(date)
  def bucket_day(date: Date): String = BucketToDayFormatter.format(date)
}
