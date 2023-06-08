import Dependencies._

name := "akka-serialization"

organization := "com.evolutiongaming"

homepage := Some(new URL("http://github.com/evolution-gaming/akka-serialization"))

startYear := Some(2018)

organizationName := "Evolution"

organizationHomepage := Some(url("http://evolution.com"))

scalaVersion := crossScalaVersions.value.head

crossScalaVersions := Seq("2.13.11", "2.12.13")

publishTo := Some(Resolver.evolutionReleases)

libraryDependencies ++= Seq(
  Akka.actor,
  Scodec.core,
  Scodec.bits,
  scalatest % Test)

licenses := Seq(("MIT", url("https://opensource.org/licenses/MIT")))

releaseCrossBuild := true

Compile / doc / scalacOptions ++= Seq("-groups", "-implicits", "-no-link-warnings")

scalacOptsFailOnWarn := Some(false)