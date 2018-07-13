package com.gensler.scalavro.test

import com.typesafe.scalalogging.LazyLogging
import org.scalatest.FlatSpec
import org.scalatest.Matchers
import spray.json.{ JsValue, PrettyPrinter }

trait AvroSpec extends FlatSpec with Matchers with LazyLogging {

  protected def prettyPrint(json: JsValue) {
    val buff = new java.lang.StringBuilder
    PrettyPrinter.print(json, buff)
    logger info buff.toString
  }

}
