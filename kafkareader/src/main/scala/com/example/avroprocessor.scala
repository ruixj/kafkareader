package com.example

import org.apache.avro.Schema
import org.apache.log4j.LogManager
import scala.collection.mutable


object AvroProcessor {
  lazy val logger = LogManager.getLogger("SchemaTransform")

  def extractFieldType(schema: Schema): mutable.Map[String, String] = {
    val fieldType: mutable.Map[String, Any] = SchemaTree.toExtractedType(schema)
    //note: flattened field2type map helps  type lookup performance
    flatFieldType("", fieldType, mutable.Map.empty[String, String])
  }

  def flatFieldType(parent: String, fieldType: mutable.Map[String, Any], result: mutable.Map[String, String]): mutable.Map[String, String] = {
    fieldType.foreach { kv =>
      val key = kv._1
      val value = kv._2
      val combinedKey = if (parent.isEmpty) key else parent + "/" + key
      value match {
        case s: mutable.HashMap[String, Any] => flatFieldType(combinedKey, s, result)
        case string: String => result += (combinedKey -> string)
        case _ =>
      }
    }
    result
  }
}
