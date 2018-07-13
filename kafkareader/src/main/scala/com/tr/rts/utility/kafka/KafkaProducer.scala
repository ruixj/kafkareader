/////////////////////////////////////////////////////////
//
// File: KafkaProducer.scala
// Description: Source code of KafkaProducer
//
//
// Copyright (c) 2015 by Thomson Reuters. All rights reserved.
//
// No portion of this software in any form may be used or
// reproduced in any manner without written consent from
// Thomson Reuters
//

package com.tr.rts.utility.kafka

import java.util.Properties
import java.util.concurrent.Future
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord, RecordMetadata}

case class CATKafkaProducer(brokers: String,
                         conf: Properties = new Properties) {
  type Key = Array[Byte]
  type Val = Array[Byte]

  val effectiveConfig = {
    val p = new Properties
    p.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers)
    p.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.ByteArraySerializer")
    p.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.ByteArraySerializer")
    p.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy")
    p.put(ProducerConfig.ACKS_CONFIG, "1")
    p.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, "1")  // order is guaranteed when retry
    p.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, "200000000")
    p.put(ProducerConfig.LINGER_MS_CONFIG, "100")
    p.put(ProducerConfig.BATCH_SIZE_CONFIG, "1000")
    p.put(ProducerConfig.BUFFER_MEMORY_CONFIG, "500000000")
    p.put(ProducerConfig.RETRIES_CONFIG, "5")
    p.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, "1000")
    p.put(ProducerConfig.TIMEOUT_CONFIG, "60000")
    p.putAll(conf)
    p
  }

  val producer = new KafkaProducer[Array[Byte], Array[Byte]](effectiveConfig)

  /**
    * send a single message, synchronized
    *
    * @param topic
    * @param key
    * @param value
    * @return
    */
  def send(topic: String, key: Key, value: Val) = {
    val m = new ProducerRecord[Key, Val](topic, key, value)
    producer.send(m).get()
  }

  /**
    * send list of messages with specified topic for each
    * will wait on response for last message
    * @param topicKeyValueMessages list of (topic, key, value)
    */
  def batchSend(topicKeyValueMessages: Iterable[(String, Key, Val)]): Unit  = {
    if(topicKeyValueMessages.isEmpty){
      return
    }

    var res: Future[RecordMetadata] = null
        topicKeyValueMessages.foreach { case(topic, key, value) =>
      val m = new ProducerRecord[Key, Val](topic, key, value)
      res = producer.send(m)
    }
    val metadata = res.get()
    if(metadata == null)
      throw new Exception("Fail to send message to Kafka.")
  }

  def batchSend(topicKeyValueMessages: Iterator[(String, Key, Val)]): Unit  = {
    var res: Future[RecordMetadata] = null
    var cnt: Int = 0
    topicKeyValueMessages.foreach { case(topic, key, value) =>
      val m = new ProducerRecord[Key, Val](topic, key, value)
      res = producer.send(m)
      cnt += 1
    }
    if(cnt > 0) {
      val metadata = res.get()
      if(metadata == null)
        throw new Exception("Fail to send message to Kafka.")
    }
  }

  /**
    * send list of messages of same topic, asynchronized
    * will wait on response for last message
    * @param topic
    * @param keyValueMessages list of (key, message)
    */
  def batchSend(topic: String, keyValueMessages: Iterable[(Key, Val)]): Unit = {
    batchSend(keyValueMessages.map { case (key, value) => (topic, key, value)})
  }

  def close(): Unit = producer.close()
}
