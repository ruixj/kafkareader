/////////////////////////////////////////////////////////
//
// File: AvroMessage.scala
// Description: Source code of AvroMessage
//
//
// Copyright (c) 20115 by Thomson Reuters. All rights reserved.
//
// No portion of this software in any form may be used or
// reproduced in any manner without written consent from
// Thomson Reuters
//

package com.tr.rts.utility.kafka.message

import java.io.{ByteArrayInputStream, ByteArrayOutputStream}

import com.gensler.scalavro.types.AvroType
import scala.util.Try

object AvroMessage {

  object MsgOperation extends Enumeration {
    type MsgOperation = Value
    val Delete = Value("delete")
    val Modify = Value("modify")
    val Insert = Value("insert")
    val Empty = Value("empty")
  }

  case class Message(dataDomain: String,
                     dataType: String,
                     updateType: String,
                     newVal: Option[Seq[Byte]],
                     oldVal: Option[Seq[Byte]],
                     time: Long,
                     key: String) {

    def getMsgType(): MsgOperation.Value = {
      if (MsgOperation.Delete.toString == updateType) {
        if (oldVal.isEmpty || oldVal.get.isEmpty)
          MsgOperation.Empty
        else
          MsgOperation.Delete
      } else if (MsgOperation.Insert.toString == updateType) {
        if (newVal.isEmpty || newVal.get.isEmpty)
          MsgOperation.Empty
        else {
          MsgOperation.Insert
        }
      } else if (MsgOperation.Modify.toString == updateType) {
        if (newVal.isEmpty || newVal.get.isEmpty) {
          if (oldVal.isEmpty || oldVal.get.isEmpty)
            MsgOperation.Empty
          else
            MsgOperation.Delete
        } else {
          if (oldVal.isEmpty || oldVal.get.isEmpty)
            MsgOperation.Insert
          else
            MsgOperation.Modify
        }
      } else
        MsgOperation.Empty
    }
  }

  case class GroupMessage(messages: Seq[Message])

  val Type = AvroType[GroupMessage]
  val Schema = Type.schema

  def encode(ms: GroupMessage): Array[Byte] = {
    val out = new ByteArrayOutputStream()
    Type.io.write(ms, out)
    out.toByteArray
  }

  def decode(bytes: Array[Byte]): Try[GroupMessage] = {
    val in = new ByteArrayInputStream(bytes)
    Type.io.read(in)
  }

  /**
    * Generate a key for avro message
    * @param row HBase row key
    * @param col HBase qualifier
    * @param salt true if the row is salted otherwise false
    * @return key of given HBase row key and qualifier
    */
  def getKey(row: String, col: String, salt: Boolean = true): String = {
    var r = row
    if(salt){
      r = row.substring(1)
    }
    r
  }

  def schemaJson = Schema.prettyPrint
}
