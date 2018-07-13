package com.gensler.scalavro.io.complex.test

import java.io.{ PipedInputStream, PipedOutputStream }

import com.gensler.scalavro.io.AvroTypeIO
import com.gensler.scalavro.types._
import org.scalatest.{ FlatSpec, Matchers }

import scala.util.Success

// for testing
case class Person(name: String, age: Int)

// for testing
case class SantaList(nice: Seq[Person], naughty: Seq[Person])

case class Knight(name: String, mana: Option[Int] = None)

class AvroRecordIOSpec extends FlatSpec with Matchers {

  "AvroRecordIO" should "be the AvroTypeIO for AvroRecord" in {
    val personIO = AvroType[Person].io
    val avroTypeIO: AvroTypeIO[_] = personIO
    avroTypeIO should equal (personIO)
  }

  it should "read and write simple records" in {
    val out = new PipedOutputStream
    val in = new PipedInputStream(out)

    val personIO = AvroType[Person].io

    val julius = Person("Julius Caesar", 2112)

    personIO.write(julius, out)
    personIO read in should equal (Success(julius))
  }

  it should "read and write complex record instances" in {
    val out = new PipedOutputStream
    val in = new PipedInputStream(out)

    val santaListIO = AvroType[SantaList].io

    val sList = SantaList(
      nice = Seq(Person("Suzie", 9)),
      naughty = Seq(Person("Tommy", 7), Person("Eve", 3))
    )

    santaListIO.write(sList, out)
    santaListIO read in should equal (Success(sList))
  }

  it should "read and write HandshakeRequest instances" in {
    import com.gensler.scalavro.protocol.{ HandshakeRequest, MD5 }

    val out = new PipedOutputStream
    val in = new PipedInputStream(out)

    val handshakeRequestIO = AvroType[HandshakeRequest].io

    val request = HandshakeRequest(
      clientHash = MD5("abcd1234defg5678".getBytes.toIndexedSeq),
      clientProtocol = Some("{}"), // None,
      serverHash = MD5("abcd1234defg5678".getBytes.toIndexedSeq),
      meta = Some(Map[String, Seq[Byte]]()) // None
    )

    handshakeRequestIO.write(request, out)
    val readResult = (handshakeRequestIO read in).get
    readResult should equal (request)
  }

  it should "read and write an object graph with shared references" in {
    val out = new PipedOutputStream
    val in = new PipedInputStream(out)

    val santaListIO = AvroType[SantaList].io

    val suzie = Person("Suzie", 9)

    val sList = SantaList(
      nice = Seq(suzie, Person("Dennis", 4)),
      naughty = Seq(Person("Tommy", 7), suzie, Person("Eve", 3))
    )

    santaListIO.write(sList, out)
    santaListIO read in should equal (Success(sList))
  }

  it should "read and write instances of recursively defined case classes" in {
    val out = new PipedOutputStream
    val in = new PipedInputStream(out)

    import com.gensler.scalavro.test.{ SinglyLinkedStringList => LL }

    val listIO = AvroType[LL].io

    val myList = LL(
      "one",
      Some(LL(
        "two",
        Some(LL(
          "three",
          None
        ))
      ))
    )

    listIO.write(myList, out)
    val Success(readResult) = listIO read in
    readResult should equal (myList)
  }

  private def check(knight: Knight) = {
    val knightIO = AvroType[Knight].io
    val json = knightIO writeJson knight
    knightIO readJson json should equal (Success(knight))
  }

  it should "read and write simple records with some optional field as JSON" in {
    check(Knight("lancelot", Option(3456)))
  }

  it should "read and write simple records with missing optional field as JSON" in {
    check(Knight("lancelot", None))
  }

  it should "read and write simple records as JSON" in {
    val personIO = AvroType[Person].io

    val julius = Person("Julius Caesar", 2112)
    val json = personIO writeJson julius
    personIO readJson json should equal (Success(julius))
  }

  it should "read and write complex record instances as JSON" in {
    val santaListIO = AvroType[SantaList].io

    val sList = SantaList(
      nice = Seq(Person("Suzie", 9)),
      naughty = Seq(Person("Tommy", 7), Person("Eve", 3))
    )

    val json = santaListIO writeJson sList
    santaListIO readJson json should equal (Success(sList))
  }
}