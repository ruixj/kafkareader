package com.gensler.scalavro.test

import com.gensler.scalavro.test.Direction.Direction
import com.gensler.scalavro.types.AvroType
import com.jayway.jsonpath.JsonPath

class AvroEnumSpec extends AvroSpec {

  it should "generate self-contained schema for enumeration" in {
    val directionType = AvroType[Direction]
    val json = JsonPath.parse(directionType.selfContainedSchema().toString())
    json.read[String]("$.name") should be ("Direction")
  }
}
