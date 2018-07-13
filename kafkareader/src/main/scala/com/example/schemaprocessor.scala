package com.example
import org.apache.avro.Schema
import org.apache.log4j.LogManager

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.util.control.Breaks._



object SchemaTree {
  lazy val logger = LogManager.getLogger("SchemaTree")
  def toExtractedType(schema: Schema): mutable.Map[String, Any] = {
    if (schema.getType != Schema.Type.RECORD)
      mutable.Map.empty[String, Any]
    else {
      toType(schema).asInstanceOf[mutable.Map[String, Any]]
    }
  }

  private def toType(schema: Schema): Any = {
    schema.getType match {
      case Schema.Type.RECORD =>
        val m = mutable.Map.empty[String, Any]
        val fields = schema.getFields
        for (index <- 0 until fields.size()) {
          val field = fields.get(index)
          val t = toType(field.schema())
          m += (field.name() -> t)
        }
        m
      case Schema.Type.ARRAY =>
        val s = schema.getElementType();
        val ret = toType(s);
        ret match {
          case e: mutable.HashMap[String, Any] =>
            e += ("_list" -> true)
          case _ =>
        }
        ret
      case Schema.Type.MAP =>
        schema.getValueType.toString() + "_map"
      case Schema.Type.UNION =>
        val ss = schema.getTypes
        val s = ss.get(1)
        toType(s)
      case Schema.Type.LONG =>
        "long"
      case Schema.Type.DOUBLE =>
        "double"
      case Schema.Type.STRING =>
        "string"
      case _ =>
    }
  }

}
