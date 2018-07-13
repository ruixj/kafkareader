package testing.a {
  trait A {
  }
}

package testing.b {
  case class B(a: Int) extends testing.a.A {
  }

  trait A extends testing.a.A {
  }
}

package testing.c {
  case class C(a: String) extends testing.a.A {
  }

  case class B(a: Long) extends testing.b.A {
  }

  case class A(a: Double) extends testing.a.A {
  }
}

package com.gensler.scalavro.test {

  import scala.collection.mutable
  import scala.util.{ Try, Success, Failure }
  import scala.reflect.runtime.universe._

  import com.gensler.scalavro.types._
  import com.gensler.scalavro.types.complex.AvroRecord
  import com.gensler.scalavro.error._

  import java.io.{ ByteArrayInputStream, ByteArrayOutputStream }

  class AvroRecordSpec extends AvroSpec {

    val personType = AvroType[Person]
    val santaListType = AvroType[SantaList]

    "AvroRecord" should "be parameterized with its corresponding Scala type" in {
      personType.isInstanceOf[AvroType[Person]] should be (true)
      typeOf[personType.scalaType] =:= typeOf[Person] should be (true)
    }

    it should "be a complex AvroType" in {
      personType.isPrimitive should be (false)
      santaListType.isPrimitive should be (false)
    }

    it should "output a fully self-contained schema" in {

      santaListType match {
        case recordType: AvroRecord[_] => {
          // println("santaListType.selfContainedSchema:")
          // println(recordType.selfContainedSchema())
        }
      }

    }

    it should "ensure strict ordering of derived sub types" in {
      val AType = AvroType[testing.a.A]

      AType.parsingCanonicalForm.toString should equal ("""[[{"type":"record","name":"testing.b.B","fields":[{"type":"int","name":"a"}]},{"type":"record","name":"com.gensler.scalavro.Reference","fields":[{"type":"long","name":"id"}]}],[{"type":"record","name":"testing.c.A","fields":[{"type":"double","name":"a"}]},"com.gensler.scalavro.Reference"],[{"type":"record","name":"testing.c.B","fields":[{"type":"long","name":"a"}]},"com.gensler.scalavro.Reference"],[{"type":"record","name":"testing.c.C","fields":[{"type":["null","string"],"name":"a"}]},"com.gensler.scalavro.Reference"]]""")
    }

  }

}