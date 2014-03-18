import AssemblyKeys._

assemblySettings

name := "BTCTurkTracker"

version := "0.1"

scalaVersion := "2.10.3"

libraryDependencies ++= Seq(
  "ch.qos.logback" % "logback-classic" % "1.1.1",
  "com.beust" % "jcommander" % "1.35",
  "com.fasterxml.jackson.module" % "jackson-module-scala_2.10" % "2.3.1",
  "net.databinder.dispatch" %% "dispatch-core" % "0.11.0",
  "org.scalatest" % "scalatest_2.10" % "2.1.0" % "test",
  "org.slf4j" % "slf4j-api" % "1.7.6")
