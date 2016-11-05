organization := """com.hunorkovacs"""

name := """itsmycrib"""

version := "1.0.0-SNAPSHOT"

scalaVersion := "2.12.0"

resolvers ++= Seq(
  "Typesafe Repo" at "http://repo.typesafe.com/typesafe/releases/",
  "Sonatype OSS Releases" at "https://oss.sonatype.org/content/repositories/releases/"
)

libraryDependencies ++= Seq(
  "com.typesafe.akka" % "akka-actor_2.12.0-RC2" % "2.4.12",
  "com.typesafe.akka" % "akka-http-experimental_2.12.0-RC2" % "2.4.11",
  "com.typesafe.akka" % "akka-http-spray-json-experimental_2.12.0-RC2" % "2.4.11",
  "org.slf4j" % "slf4j-api" % "1.7.21",
  "ch.qos.logback" % "logback-classic" % "1.1.7"
)
