import sbt.*

object Dependencies {

  val scalatest = "org.scalatest" %% "scalatest" % "3.2.19"

  object Akka {
    private val version = "2.6.21"
    val actor = "com.typesafe.akka" %% "akka-actor" % version
  }

  object Scodec {
    val core = "org.scodec" %% "scodec-core" % "2.3.2"
    val core2 = "org.scodec" %% "scodec-core" % "1.11.10"
  }
}
