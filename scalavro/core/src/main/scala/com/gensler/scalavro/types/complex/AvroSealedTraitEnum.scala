package com.gensler.scalavro.types.complex

import com.gensler.scalavro.JsonSchemaProtocol._
import com.gensler.scalavro.types.{ AvroNamedType, AvroType }
import com.gensler.scalavro.util.ReflectionHelpers
import spray.json._

import scala.collection.immutable.ListMap
import scala.collection.mutable
import scala.reflect.runtime.universe._

class AvroSealedTraitEnum[E: TypeTag](
    val name: String,
    val symbols: Map[String, E],
    val namespace: Option[String] = None) extends AvroNamedType[E] {

  /**
    * Returns the Avro type name for this schema.
    */
  override val typeName = "enum"

  /**
    * Returns the fully self-describing JSON representation of this Avro type
    * schema.
    */
  override def selfContainedSchema(resolvedSymbols: mutable.Set[String] = mutable.Set[String]()) = {

    val requiredParams = ListMap(
      "name" -> name.toJson,
      "type" -> typeName.toJson,
      "symbols" -> symbols.keys.toList.toJson
    )

    val optionalParams = ListMap(
      "namespace" -> namespace
    ).collect { case (k, Some(v)) => (k, v.toJson) }

    new JsObject(requiredParams ++ optionalParams)
  }
}

object AvroSealedTraitEnum {
  private[types] def fromType[T: TypeTag](processedTypes: Set[Type]): AvroType[T] = {
    val tt = typeTag[T]
    val namespace = tt.tpe.typeSymbol.owner.fullName
    val (name, values) = ReflectionHelpers.nameAndValues[T]
    new AvroSealedTraitEnum[T](name = name, symbols = values, namespace = Some(namespace))
  }
}