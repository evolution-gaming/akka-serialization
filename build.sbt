import Dependencies._

name := "akka-serialization"

organization := "com.evolutiongaming"

homepage := Some(new URL("http://github.com/evolution-gaming/akka-serialization"))

startYear := Some(2018)

organizationName := "Evolution Gaming"

organizationHomepage := Some(url("http://evolutiongaming.com"))

bintrayOrganization := Some("evolutiongaming")

scalaVersion := crossScalaVersions.value.head

crossScalaVersions := Seq("2.12.9")

resolvers += Resolver.bintrayRepo("evolutiongaming", "maven")

libraryDependencies ++= Seq(
  Akka.actor,
  Scodec.core,
  Scodec.bits,
  scalatest % Test)

licenses := Seq(("MIT", url("https://opensource.org/licenses/MIT")))

releaseCrossBuild := true

scalacOptions in (Compile,doc) ++= Seq("-groups", "-implicits", "-no-link-warnings")