package com.uwola.scala

import java.util.Date

import scala.beans.BeanProperty

/**
  * Created by uwoer on 2018/6/1.
  */
class VisitEvent extends SimpleEvent{
  @BeanProperty var ip: String = ""
  @BeanProperty var countryName: String = ""
  @BeanProperty var region: String = ""
  @BeanProperty var city: String = ""
  @BeanProperty var domain: String = ""
  @BeanProperty var path: String = ""
  @BeanProperty var refer: String = ""
  @BeanProperty var userAgent: String = ""
  @BeanProperty var appVersion: String = ""
  @BeanProperty var model: String = ""
  @BeanProperty var manufacturer: String = ""
  @BeanProperty var channel: String = ""
  @BeanProperty var language: String = ""
  @BeanProperty var osVersion: String = ""
  @BeanProperty var resolution: String = ""
  @BeanProperty var platform: String = ""
  @BeanProperty var id: String = ""
  @BeanProperty var query: String = ""
  @BeanProperty var lat: Double = 0
  @BeanProperty var lng: Double = 0
  @BeanProperty var companyId: String = ""
  @BeanProperty var projectId: String = ""
  @BeanProperty var login_userId: String = ""

  // Convert timestamp into Time Bucket using Bucketing Strategy
  override val bucket = BucketingStrategy.bucket(new Date(eventTime))
  override val bucket_hour = BucketingStrategy.bucket_hour(new Date(eventTime))
  override val bucket_day = BucketingStrategy.bucket_day(new Date(eventTime))

}
