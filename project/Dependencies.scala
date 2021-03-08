import sbt._

object Dependencies {

  val scalatest = "org.scalatest" %% "scalatest" % "3.2.6"

  object Akka {
    private val version = "2.6.0"
    val actor = "com.typesafe.akka" %% "akka-actor" % version
  }

  object Scodec {
    val core = "org.scodec" %% "scodec-core" % "1.11.7"
    val bits = "org.scodec" %% "scodec-bits" % "1.1.14"
  }
}
