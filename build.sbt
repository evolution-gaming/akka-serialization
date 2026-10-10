import Dependencies.*

name := "akka-serialization"

organization := "com.evolutiongaming"

homepage := Some(uri("https://github.com/evolution-gaming/akka-serialization"))

startYear := Some(2018)

organizationName := "Evolution"

organizationHomepage := Some(uri("https://evolution.com"))

crossScalaVersions := Seq("3.9.0", "2.13.18")
scalaVersion := crossScalaVersions.value.head

scalacOptions ++= {
  CrossVersion.partialVersion(scalaVersion.value) match {
    case Some((2, 13)) => Seq("-Xsource:3", "-Ytasty-reader")
    case _ => Seq.empty
  }
}
//see https://github.com/scodec/scodec/issues/365
libraryDependencies ++=
  (CrossVersion.partialVersion(scalaVersion.value) match {
    case Some((3, _)) =>
      Seq(
        Scodec.coreScala3 % Optional,
      )
    case _ =>
      Seq(
        Scodec.coreScala2 % Optional,
      )
  })
libraryDependencies ++= Seq(
  Akka.actor,
  scalatest % Test,
)

publishTo := Some(Resolver.evolutionReleases)

licenses := Seq(("MIT", uri("https://opensource.org/licenses/MIT")))

Compile / doc / scalacOptions ++= Seq("-groups", "-implicits", "-no-link-warnings")

addCommandAlias("check", "+all versionPolicyCheck scalafmtCheckRepo Compile/doc")
addCommandAlias("fmt", "+scalafmtRepo")
addCommandAlias("build", "+all test package")

// Your next release will be binary compatible with the previous one,
// but it may not be source compatible (ie, it will be a minor release).
ThisBuild / versionPolicyIntention := Compatibility.BinaryCompatible

ThisBuild / versionPolicyIgnored ++= Seq(
  /*
  Examples:

  //com.chuusai:shapeless_2.13: missing dependency
  "com.chuusai" %% "shapeless",
  //org.scala-lang.modules:scala-java8-compat_2.13:
  //  incompatible version change from 0.9.0 to 1.0.0 (compatibility: early semantic versioning)
  "org.scala-lang.modules" %% "scala-java8-compat",
   */
)
