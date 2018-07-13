/* Copyright (c) 2016 Thomson Reuters.
 * All rights reserved.
 *
 * No portion of this software in any form may be used
 * or reproduced in any manner without written consent
 * from Thomson Reuters.
 */

package com.tr.rts.utility.kafka

import java.nio.charset.StandardCharsets

object Converter {

  def toBytes(str: String): Array[Byte] = str.getBytes(StandardCharsets.UTF_8)
  def toString(bytes: Array[Byte]): String = new String(bytes, StandardCharsets.UTF_8)

}
