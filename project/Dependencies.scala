import sbt.*

object Dependencies {

  val scalatest = "org.scalatest" %% "scalatest" % "3.2.20"

  object Akka {
    private val version = "2.6.21"
    val actor = "com.typesafe.akka" %% "akka-actor" % version
  }

  object Scodec {
    val coreScala3 = "org.scodec" %% "scodec-core" % "2.3.3"
    val coreScala2 = "org.scodec" %% "scodec-core" % "1.11.11"
  }
}
