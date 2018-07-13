/////////////////////////////////////////////////////////
//
// File: Build.scala
// Description: Source code of Project Build
//
//
// Copyright (c) 2015 by Thomson Reuters. All rights reserved.
//
// No portion of this software in any form may be used or
// reproduced in any manner without written consent from
// Thomson Reuters
//


import sbt.Keys._
import sbt._



object RTSP0Build extends Build {
  lazy val root = Project(id = "TestKafka",
    base = file(".")) aggregate(
    scalavro_util,
    scalavro_core,
    lib_kafka_reader


  )


  val Org = "com.tr.rts"
  val TheVersion = "1.0.0"
  val ScalaVersion = "2.11.11"
  val scalaVersionMajor = "2.11"

  val xercesVersion = "2.11.0"
  val jlibsVersion = "2.2.1"
  val scallopVersion = "2.0.1"
  val PackageDir = file("dscat-deploy").getAbsolutePath


  /// version + jenkins version

  //////////////////////////////////////////////////////////
  //
  // dependencies
  //
  //////////////////////////////////////////////////////////
  lazy val hadoop_version = "2.6.0-cdh5.5.2"
  lazy val hbase_version = "1.0.0-cdh5.5.2"
  /* TODO: Spark 1.5.1 depends on hadoop-client 2.2.0 which is not same as hadoop_version 2.6.0-cdh5.4.4. We need to change the version later. Normally, we need to upgrade to cdh5.5.x probably */
  lazy val spark_version = "2.1.1"
  lazy val elasticsearch_version = "2.3.2"

  // test support
  lazy val junit = "junit" % "junit" % "4.12" % Test
  lazy val junit_interface = "com.novocode" % "junit-interface" % "0.11" % Test

  lazy val scalacheck = "org.scalacheck" %% "scalacheck" % "1.13.5" % Test
  lazy val scalamock = "org.scalamock" %% "scalamock-scalatest-support" % "3.6.0" % Test
  lazy val spark_testing = "com.holdenkarau" %% "spark-testing-base" % "2.1.0_0.6.0" % Test
  lazy val jackson_databind_override = "com.fasterxml.jackson.core" % "jackson-databind" % "2.4.4" % Test

  // elasticsearch
  lazy val elasticsearchFilePath = s"$PackageDir/jars/elasticsearch-" + elasticsearch_version + ".jar"
  lazy val elasticsearch = "org.elasticsearch" % "elasticsearch" % elasticsearch_version
  lazy val elasticsearch_hadoop = "org.elasticsearch" %% "elasticsearch-spark-20" % "5.4.0"
  lazy val jest = "io.searchbox" % "jest" % "5.3.3"

  // hadoop
  lazy val hbase_common = "org.apache.hbase" % "hbase-common" % hbase_version
  lazy val hbase_client = "org.apache.hbase" % "hbase-client" % hbase_version
  lazy val hbase_server = "org.apache.hbase" % "hbase-server" % hbase_version
  lazy val hadoop_common = "org.apache.hadoop" % "hadoop-common" % hadoop_version


  lazy val hadoop_hdfs = "org.apache.hadoop" % "hadoop-hdfs" % hadoop_version


  lazy val hbase_hadoop_compat = "org.apache.hbase" % "hbase-hadoop-compat" % hbase_version
  lazy val zookeeper = "org.apache.zookeeper" % "zookeeper" % "3.4.5-cdh5.5.2"

  // spark
  lazy val spark_core = "org.apache.spark" %% "spark-core" % spark_version % "provided"
  lazy val spark_sql = "org.apache.spark" %% "spark-sql" % spark_version % "provided"
  lazy val spark_hive = "org.apache.spark" %% "spark-hive" % spark_version % "provided"
  lazy val spark_streaming = "org.apache.spark" %% "spark-streaming" % spark_version % "provided"
  lazy val spark_streaming_kafka = "org.apache.spark" %% "spark-streaming-kafka-0-8" % spark_version
  lazy val bijection_avro = "com.twitter" %% "bijection-avro" % "0.9.2"

  // akka
  lazy val akka_actor = "com.typesafe.akka" %% "akka-actor" % "2.3.11"

  // hbase
  lazy val hbase_rdd = "eu.unicredit" %% "hbase-rdd" % "0.7.1"

  // avro
  val avro_version = "1.7.6-cdh5.5.2"
  lazy val avro = "org.apache.avro" % "avro" % avro_version

  // project config
  lazy val configFileGroupId = "com.tr.rts"
  lazy val configFileProjectName = "ProjectConfig"
  lazy val configFileCurrentVersion = "1.2"
  lazy val configFileVersion = configFileGroupId % configFileProjectName % configFileCurrentVersion
  lazy val configFilePath = s"$PackageDir/config/" + configFileProjectName + "-" + configFileCurrentVersion + ".jar"
  lazy val configFileXmlPath = s"$PackageDir/config/" + configFileProjectName + ".xml"

  // kafka
//  lazy val kafka = "org.apache.kafka" %% "kafka" % "0.8.2.1"
  lazy val kafka = "org.apache.kafka" %% "kafka" % "0.9.0.1"
  lazy val kafkaclient =  "org.apache.kafka" % "kafka-clients" % "0.9.0.1"
  lazy val zkclient = "com.101tec" % "zkclient" % "0.3" // TODO: upgrade to 0.7

  // json
  lazy val json4s = "org.json4s" %% "json4s-jackson" % "3.2.11"
  // this is the version in Spark 1.5.1
  lazy val spray_json = "io.spray" %% "spray-json" % "1.3.3"
  lazy val org_json = "org.json" % "json" % "20131018"
  lazy val gson = "com.google.code.gson" % "gson" % "2.3.1"
  lazy val quick_json = "com.codesnippets4all" % "quick-json" % "1.0.4"
  lazy val simple_json = "com.googlecode.json-simple" % "json-simple" % "1.1"
  lazy val jolt_core = "com.bazaarvoice.jolt" % "jolt-core" % "0.0.20"
  lazy val jolt_util = "com.bazaarvoice.jolt" % "json-utils" % "0.0.20"
  lazy val jayway = "com.jayway.jsonpath" % "json-path" % "2.2.0"
  lazy val diffson = "org.gnieh" %% "diffson" % "1.0.0"

  // scala
  lazy val scalaj_http = "org.scalaj" %% "scalaj-http" % "1.0.1"
  lazy val scala_arm = "com.jsuereth" %% "scala-arm" % "1.4"
  lazy val scala_reflect = "org.scala-lang" % "scala-reflect" % ScalaVersion
  lazy val scala_xml = "org.scala-lang.modules" %% "scala-xml" % "1.0.6"
  lazy val reflections = "org.reflections" % "reflections" % "0.9.9-RC1"
  lazy val typesafe_config = "com.typesafe" % "config" % "1.3.1"

  // java
  lazy val common_lang3 = "org.apache.commons" % "commons-lang3" % "3.3.2"
  lazy val common_io = "commons-io" % "commons-io" % "2.4"
  lazy val zip4j = "net.lingala.zip4j" % "zip4j" % "1.3.2"
  lazy val joda_time = "joda-time" % "joda-time" % "2.7"
  lazy val slf4j = "org.slf4j" % "slf4j-log4j12" % "1.7.5"
  //lazy val jersey = "com.sun.jersey" % "jersey-server" % "1.12"
  lazy val jersey = "org.glassfish.jersey.core" % "jersey-server" % "2.22.2"
  lazy val jersey_client = "org.glassfish.jersey.core" % "jersey-client" % "2.22.2"
  lazy val jersey_container_servlet = "org.glassfish.jersey.containers" % "jersey-container-servlet-core" % "2.22.2"
  lazy val jersey_jackson1x = "org.glassfish.jersey.media" % "jersey-media-json-jackson1" % "2.22.2"
  lazy val swagger = "io.swagger" % "swagger-jersey-jaxrs" % "1.5.0"

  lazy val apache_http = "org.apache.httpcomponents" % "httpclient" % "4.3.3"
  lazy val protobuf = "com.google.protobuf" % "protobuf-java" % "2.6.1"
  lazy val htrace = "org.htrace" % "htrace-core" % "3.0.4"

  lazy val servlet_api = "javax.servlet" % "javax.servlet-api" % "3.1.0"

  lazy val adc_logging_FileVersion = "com.adc.logging" % "TramsLogger" % "0.2.1"
  lazy val adc_logging = "/../project/extraJars/adc-logging-assembly-0.2.1.jar"

  lazy val jackson = "com.fasterxml.jackson.core" % "jackson-core" % "2.8.8"

  lazy val jenaDependencies = Seq(
    "com.sun.xsom" % "xsom" % "20110809",
    "org.apache.jena" % "jena-core" % "2.12.1",
    "org.apache.xmlbeans" % "xmlbeans" % "2.4.0"
  )

  lazy val scalatest = "org.scalatest" %% "scalatest" % "2.2.6" % Test
  lazy val scalatest_ = "org.scalatest" %% "scalatest" % "2.2.6"
  lazy val antlr_runtime = "org.antlr" % "antlr4-runtime" % "4.7"

  lazy val javaxJSON = "org.glassfish" % "javax.json" % "1.1" % "provided"
  lazy val log_json = "org.json" % "json" % "20070829"
  lazy val kafkaAppender = "org.apache.kafka" % "kafka-log4j-appender" % "0.9.0.0" % "provided"

  lazy val scala_logging_slf4j = "com.typesafe.scala-logging" %% "scala-logging-slf4j" % "2.1.2"
  lazy val scala_logging = "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0"

  lazy val slf4j_api = "org.slf4j" % "slf4j-api" % "1.7.18"
  lazy val slf4j_simple = "org.slf4j" % "slf4j-simple" % "1.7.18"


  //jlibs, used to generate sample xml from xsd
  lazy val xerces = "xerces" % "xercesImpl" % xercesVersion
  lazy val jlibsCore = "in.jlibs" % "jlibs-core" % jlibsVersion
  lazy val jlibsXML = "in.jlibs" % "jlibs-xml" % jlibsVersion
  lazy val jlibsXSD = "in.jlibs" % "jlibs-xsd" % jlibsVersion
  lazy val scallop = "org.rogach" %% "scallop" % scallopVersion
  //////////////////////////////////////////////////////////
  //
  // repositories
  //
  //////////////////////////////////////////////////////////
  lazy val common_resolvers = Seq(
    // add link ~/.m2/repository to actual maven repo
    // if not use default repo location
    "central" at "http://central.maven.org/maven2/",
    "maven-hadoop" at "https://repository.cloudera.com/content/repositories/releases/",
    "cloudera" at "https://repository.cloudera.com/artifactory/cloudera-repos/",
    "Spray Micro-Repository" at "http://repo.spray.io/",
    Resolver.mavenLocal
  )

  //////////////////////////////////////////////////////////
  //
  // common settings
  //
  //////////////////////////////////////////////////////////


  lazy val commonSettings = Seq(
    organization := Org,
    scalaVersion := ScalaVersion,
    version := TheVersion,
    parallelExecution in Test := false,
    resolvers ++= common_resolvers,
    libraryDependencies += scalatest,
    libraryDependencies += junit_interface,
    testOptions += Tests.Argument(TestFrameworks.JUnit, "+q", "-v"),
    dependencyOverrides += slf4j,

    dependencyOverrides += "com.google.guava" % "guava" % "12.0.1",
    dependencyOverrides += avro, // to avoid problem by ivy: java.lang.IllegalStateException: impossible to get artifacts when data has not been loaded. IvyNode = org.apache.avro#avro;1.7.4

    crossTarget in packageBin := file(s"$PackageDir/package-jars/"),
    scalacOptions ++= Seq("-feature", "-deprecation"),
    javaOptions ++= Seq("-Xms512M", "-Xmx4096M", "-XX:+CMSClassUnloadingEnabled")
  )

  lazy val javaSettings = Seq(
    organization := Org,
    version := TheVersion,
    scalaVersion := ScalaVersion,
    //isSnapshot := true,
    // publishMavenStyle := true,
    updateOptions := updateOptions.value.withConsolidatedResolution(true),
    offline := true,
    crossPaths := false,
    autoScalaLibrary := false,
    resolvers ++= common_resolvers,
    libraryDependencies += scalatest,
    libraryDependencies += junit_interface,
    testOptions += Tests.Argument(TestFrameworks.JUnit, "+q", "-v")

  )


  val packagePrefixes = "au|org|com|javax|parquet|jodd|edu"
  val fileSuffixes = "xsd|dtd|xml|html|properties|thrift"


  //////////////////////////////////////////////////////////
  //
  // sub project settings
  //
  //////////////////////////////////////////////////////////

  lazy val scalavro_util = Project(
    id = "scalavro-util",
    base = file("scalavro/util")
  ).settings(commonSettings: _*)
    .settings(
      libraryDependencies ++= Seq(
        scala_reflect,
        reflections,
        typesafe_config,
        scala_logging,
        slf4j_api,
        slf4j_simple % Test
      )
    )


  lazy val scalavro_core = Project(
    id = "scalavro-core",
    base = file("scalavro/core")
  ).settings(commonSettings: _*)
    .settings(
      libraryDependencies ++= Seq(
        avro,
        scala_xml,
        spray_json,
        jayway % Test,
        diffson % Test
      )
    ) dependsOn (scalavro_util)

  lazy val lib_kafka_reader = Project(
    id = "lib_kafka_reader",
    base = file("kafkareader")
  ).settings(commonSettings: _*)
    .settings(
      libraryDependencies ++= Seq(
        kafkaclient,
        zkclient,
        junit,
        json4s,
        avro
      )
    ) dependsOn(scalavro_core )


}
