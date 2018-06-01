package com.uwola.scala

import scala.beans.BeanProperty

/**
  * Created by uwoer on 2018/6/1.
  */
class SimpleEvent {
  @BeanProperty var userId: String = ""
  @BeanProperty var sessionId: String = ""
  @BeanProperty var sendTime: Long = 0
  @BeanProperty var eventTime: Long = 0
  @BeanProperty var eventType: String = ""
  @BeanProperty val bucket: String = ""
  @BeanProperty val bucket_hour: String = ""
  @BeanProperty val bucket_day: String = ""
}
