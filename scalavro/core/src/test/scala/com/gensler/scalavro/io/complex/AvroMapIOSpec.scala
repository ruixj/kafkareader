package com.gensler.scalavro.io.complex.test

import scala.util.{ Try, Success, Failure }
import scala.reflect.runtime.universe._

import org.scalatest.FlatSpec
import org.scalatest.Matchers

import com.gensler.scalavro.types._
import com.gensler.scalavro.types.complex._
import com.gensler.scalavro.io.complex._
import com.gensler.scalavro.error._

import java.io.{
  ByteArrayInputStream,
  ByteArrayOutputStream,
  PipedInputStream,
  PipedOutputStream
}

class AvroMapIOSpec extends FlatSpec with Matchers {

  val intMapType = AvroType.fromType[Map[String, Int]].get

  "AvroMapIO" should "be the AvroTypeIO for AvroMap" in {
    intMapType.io.isInstanceOf[AvroMapIO[_, _]] should be (true)
  }

  it should "read and write maps" in {
    val io = intMapType.io

    val m1: Map[String, Int] = Map(
      "uno" -> 1,
      "due" -> 2,
      "tre" -> 3,
      "quattro" -> 4,
      "cinque" -> 5
    )

    val out = new ByteArrayOutputStream
    io.write(m1, out)

    val in = new ByteArrayInputStream(out.toByteArray)

    val Success(readResult) = io read in

    readResult should equal (m1)
    readResult("cinque") should equal (5)
  }

  it should "read and write maps of bytes" in {
    val io = AvroType[Map[String, Seq[Byte]]].io

    val out = new PipedOutputStream
    val in = new PipedInputStream(out)

    val bytesMap: Map[String, Seq[Byte]] = Map(
      "uno" -> "one".getBytes.toSeq,
      "due" -> "two".getBytes.toSeq,
      "tre" -> "three".getBytes.toSeq,
      "quattro" -> "four".getBytes.toSeq,
      "cinque" -> "five".getBytes.toSeq
    )

    io.write(bytesMap, out)

    val Success(readResult) = io read in

    readResult should equal (bytesMap)
    readResult("due") should equal ("two".getBytes.toSeq)
  }

  it should "return properly typed Map subtypes when reading" in {
    val out = new PipedOutputStream
    val in = new PipedInputStream(out)

    import scala.collection.immutable.ListMap
    val listMapType = AvroType[ListMap[String, Int]]
    val listMapIO = listMapType.io

    val numbers = ListMap("one" -> 1, "two" -> 2, "three" -> 3)

    listMapIO.write(numbers, out)
    val Success(readResult) = listMapIO read in

    readResult should equal (numbers)
    readResult.isInstanceOf[ListMap[_, _]] should be (true)
  }

  it should "read and write maps as JSON" in {

    val io = intMapType.io

    val m1: Map[String, Int] = Map(
      "uno" -> 1,
      "due" -> 2,
      "tre" -> 3,
      "quattro" -> 4,
      "cinque" -> 5
    )

    val json = io writeJson m1
    val Success(readResult) = io readJson json
    readResult should equal (m1)
    readResult("cinque") should equal (5)
  }

  it should "not allow null keys in maps" in {
    val io = intMapType.io

    val out = new PipedOutputStream
    val in = new PipedInputStream(out)

    val map: Map[String, Int] = Map(
      "uno" -> 1,
      "due" -> 2,
      null.asInstanceOf[String] -> 3
    )

    an[AvroSerializationException[_]] should be thrownBy {
      io.write(map, out)
      val Success(readResult) = io read in
      readResult should equal (map)
    }
  }

  it should "not allow null keys in maps via JSON" in {
    val io = intMapType.io

    val map: Map[String, Int] = Map(
      "uno" -> 1,
      "due" -> 2,
      null.asInstanceOf[String] -> 3
    )

    an[AvroSerializationException[_]] should be thrownBy {
      val json = io writeJson map
      val Success(readResult) = io readJson json
      readResult should equal (map)
    }
  }

  it should "not support Maps which have non-String keys" in {
    an[IllegalArgumentException] should be thrownBy {
      AvroType.fromType[Map[Int, Int]].get
    }
  }

}