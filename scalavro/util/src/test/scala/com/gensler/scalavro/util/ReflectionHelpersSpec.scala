package com.gensler.scalavro.util.test

import org.scalatest.FlatSpec
import org.scalatest.Matchers

import scala.reflect.runtime.universe._

import com.gensler.scalavro.util.ReflectionHelpers._

sealed trait Color
object Color {
  case object Black extends Color
  case object Blue extends Color
  case object Green extends Color
}

object Direction extends Enumeration {
  type Direction = Value
  val NORTH, EAST, SOUTH, WEST = Value
}

class ReflectionHelpersSpec extends FlatSpec with Matchers {

  "The reflection helpers object" should "return the enumeration tag for a given enum value" in {
    val et = enumForValue[Direction.type#Value]
    et.tpe =:= typeOf[Direction.type] should be (true)
  }

  it should "return constructor parameters for case classes" in {
    import scala.collection.immutable.ListMap
    caseClassParamsOf[Animal] should be (ListMap("sound" -> typeTag[String]))
  }

  it should "return constructor parameters for case classes with multiple constructors" in {
    import scala.collection.immutable.ListMap
    caseClassParamsOf[Person] should have size (2)
  }

  it should "return the avro-typable subtypes of a given type" in {
    typeableSubTypesOf[A] should have size (2)
    typeableSubTypesOf[B] should have size (1)
  }

  it should "extract default values for case class parameters" in {
    val default = Exclamation(volume = 11)
    val defaultArgs = defaultCaseClassValues[Exclamation]
    defaultArgs("word") should equal (Some(default.word))
    defaultArgs("volume") should equal (None)
  }

  it should "extract name and values for sealed trait enum" in {
    val (name, values) = nameAndValues[Color]
    val m: Map[String, Color] = values
    name should equal("Color")
    m should equal(Map("Black" -> Color.Black, "Blue" -> Color.Blue, "Green" -> Color.Green))
  }

}
