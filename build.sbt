import Dependencies._

name := "akka-serialization"

organization := "com.evolutiongaming"

homepage := Some(url("http://github.com/evolution-gaming/akka-serialization"))

startYear := Some(2018)

organizationName := "Evolution"

organizationHomepage := Some(url("https://evolution.com"))

crossScalaVersions := Seq("3.3.3", "2.13.14")
scalaVersion := crossScalaVersions.value.head


scalacOptions ++= {
  CrossVersion.partialVersion(scalaVersion.value) match {
    case Some((3, _))  => Seq.empty
    case Some((2, 13)) => Seq("-Xsource:3", "-Ytasty-reader")
    case _             => Seq.empty
  }
}
//see https://github.com/scodec/scodec/issues/365
libraryDependencies ++= (CrossVersion.partialVersion(scalaVersion.value) match {
  case Some((3, _)) =>
    Seq(
      Scodec.core % Optional
    )
  case _ =>
    Seq(
      Scodec.core2 % Optional
    )
})
libraryDependencies ++= Seq(Akka.actor, scalatest % Test)

publishTo := Some(Resolver.evolutionReleases)

licenses := Seq(("MIT", url("https://opensource.org/licenses/MIT")))

releaseCrossBuild := true

Compile / doc / scalacOptions ++= Seq("-groups", "-implicits", "-no-link-warnings")

scalacOptsFailOnWarn := Some(false)

//addCommandAlias("check", "all versionPolicyCheck Compile/doc")
addCommandAlias("check", "show version")
