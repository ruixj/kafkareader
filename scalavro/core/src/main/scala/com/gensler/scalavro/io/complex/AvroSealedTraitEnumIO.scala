package com.gensler.scalavro.io.complex

import com.gensler.scalavro.error.{ AvroDeserializationException, AvroSerializationException }
import com.gensler.scalavro.io.AvroTypeIO
import com.gensler.scalavro.types.complex.{ AvroSealedTraitEnum, AvroJEnum }
import org.apache.avro.Schema
import org.apache.avro.Schema.Parser
import org.apache.avro.generic.{ GenericData, GenericDatumReader, GenericDatumWriter, GenericEnumSymbol }
import org.apache.avro.io.{ BinaryDecoder, BinaryEncoder }
import spray.json._

import scala.collection.mutable
import scala.reflect.runtime.universe.TypeTag
import scala.util.Try

case class AvroSealedTraitEnumIO[E](avroType: AvroSealedTraitEnum[E]) extends AvroTypeIO[E]()(avroType.tag) {

  protected lazy val avroSchema: Schema = (new Parser) parse avroType.selfContainedSchema().toString

  ////////////////////////////////////////////////////////////////////////////
  // BINARY ENCODING
  ////////////////////////////////////////////////////////////////////////////

  protected[scalavro] def write[T <: E: TypeTag](
    obj: T,
    encoder: BinaryEncoder,
    references: mutable.Map[Any, Long],
    topLevel: Boolean): Unit = {

    try {
      val datumWriter = new GenericDatumWriter[GenericEnumSymbol](avroSchema)
      datumWriter.write(
        new GenericData.EnumSymbol(avroSchema, obj.toString),
        encoder
      )
    }
    catch {
      case cause: Throwable =>
        throw new AvroSerializationException(obj, cause)
    }
  }

  override protected[scalavro] def read(
    decoder: BinaryDecoder,
    references: mutable.ArrayBuffer[Any],
    topLevel: Boolean) = {

    val datumReader = new GenericDatumReader[GenericEnumSymbol](avroSchema)
    datumReader.read(null, decoder) match {
      case genericEnumSymbol: GenericEnumSymbol => avroType.symbols(genericEnumSymbol.toString)
      case _                                    => throw new AvroDeserializationException[E]()(avroType.tag)
    }
  }

  ////////////////////////////////////////////////////////////////////////////
  // JSON ENCODING
  ////////////////////////////////////////////////////////////////////////////

  override def writeJson[T <: E: TypeTag](obj: T) = JsString(obj.toString)

  override def readJson(json: JsValue) = Try {
    json match {
      case JsString(valueName) => avroType.symbols(valueName)
      case _                   => throw new AvroDeserializationException[E]()(avroType.tag)
    }
  }
}