package com.uwola.learning.redis

import java.io.Serializable

import org.apache.commons.pool2.impl.GenericObjectPoolConfig
import redis.clients.jedis.JedisPool


object RedisClient extends Serializable {
//  val redisHost = "master"
//  val redisPort = 6379
//  lazy val pool = new JedisPool(new GenericObjectPoolConfig(), redisHost, redisPort, redisTimeout)
  val redisHost = "events.3cioj6.ng.0001.cnn1.cache.amazonaws.com.cn"
  val redisPort = 6379
  val redisTimeout = 30000
  lazy val pool = new JedisPool(new GenericObjectPoolConfig(), redisHost, redisPort, redisTimeout,null,3,null)

  lazy val hook = new Thread {
    override def run = {
      println("Execute hook thread: " + this)
      pool.destroy()
    }
  }
  sys.addShutdownHook(hook.run)
}