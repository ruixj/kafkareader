package com.gensler.scalavro.io.complex

import java.io.{ ByteArrayInputStream, ByteArrayOutputStream }

import com.gensler.scalavro.io.AvroTypeIO
import com.gensler.scalavro.test.{ Color, Car }
import com.gensler.scalavro.types.AvroType
import org.scalatest.{ Matchers, FlatSpec }

import scala.util.{ Failure, Success }

class AvroSealedTraitEnumIOSpec extends FlatSpec with Matchers {

  val carType = AvroType[Car]
  val io: AvroTypeIO[Car] = carType.io

  it should "read and write sealed trait enum" in {

    val car = Car("fast", Color.Black)
    val out = new ByteArrayOutputStream
    io.write(car, out)

    val in = new ByteArrayInputStream(out.toByteArray)

    io read in match {
      case Success(readResult) => readResult should equal (car)
      case Failure(ex)         => fail(ex)
    }
  }
}
