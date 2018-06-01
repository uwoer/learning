package com.uwola.scala

import com.alibaba.fastjson.JSON

import scala.util.parsing.json.JSONObject

/**
 * @author ${user.name}
 */
object JsonDemo {
  
  def main(args : Array[String]) {
    println( "Hello World!" )
    var jsonStr: String =
//      ""
      "{\"userId\":\"015260025133326\"}"
//      "{\"userId\":\"015260025133326\",\"sendTime\":1498610506898,\"eventTime\":1498610446898,\"eventType\":\"page\",\"domain\":\"www.meimeitech.com\",\"path\":\"\",\"query\":\"\",\"refer\":\"\",\"title\":\"产品列表\",\"platform\":\"Android\",\"login_userId\":\"15208834401\",\"projectId\":\"meimeitech\",\"companyId\":\"meimeitech\",\"mobile\":\"15208834401\",\"id\":\"cf4e836e-6a8e-480a-8816-1d682458684a\",\"visit_id\":\"cd57f91f-e180-417d-8497-bff9116d40cc\",\"page_cat\":\"products-list\",\"page_bline\":\"invest\"}"
//      "{\"sendTime\":1527782405519,\"eventTime\":1498610446898,\"eventType\":\"page\",\"domain\":\"www.meimeitech.com\",\"path\":\"\",\"query\":\"\",\"refer\":\"\",\"title\":\"产品列表\",\"platform\":\"Android\",\"login_userId\":\"15208834401\",\"projectId\":\"meimeitech\",\"companyId\":\"meimeitech\",\"mobile\":\"15208834401\",\"id\":\"cf4e836e-6a8e-480a-8816-1d682458684a\",\"visit_id\":\"cd57f91f-e180-417d-8497-bff9116d40cc\",\"page_cat\":\"products-list\",\"page_bline\":\"invest\"}"
    val json = JSON.parseObject(jsonStr)
//    json match {
//      case None => println("Parsing failed: " + json);None
//    }
    println(json == None)
    println(json == null)
    println(json.get("query"))
    println(json.get("mobile"))
    println(json.get("XXXX"))

    val json1=JSON.parseObject(jsonStr,classOf[VisitEvent])
    println("=====>"+json1.userId)
    json1.userId = "xxxxxx"
    println("=====>"+json1.userId)
//    println("=====>"+json1.asInstanceOf[VisitEvent])
  }

}
