package com.tr.rts.utility.kafka.message

import scala.util.Try

/**
  * Created by Yong on 1/8/2016.
  */
abstract class MessageProc {

  def processMsg(avroMsg: AvroMessage.Message): Any

}
