import sbt._

object Dependencies {

  val scalatest = "org.scalatest" %% "scalatest" % "3.0.8"

  object Akka {
    private val version = "2.5.25"
    val actor = "com.typesafe.akka" %% "akka-actor" % version
  }

  object Scodec {
    val core = "org.scodec" %% "scodec-core" % "1.11.4"
    val bits = "org.scodec" %% "scodec-bits" % "1.1.12"
  }
}
