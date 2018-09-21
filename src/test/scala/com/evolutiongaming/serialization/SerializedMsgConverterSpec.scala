package com.evolutiongaming.serialization

import akka.actor.ActorSystem
import org.scalatest.{FunSuite, Matchers}

import scala.concurrent.Await
import scala.concurrent.duration._

class SerializedMsgConverterSpec extends FunSuite with Matchers {

  test("toMsg and fromMsg") {
    val system = ActorSystem(getClass.getSimpleName)
    val converter = SerializedMsgExt(system)
    val value = "value"
    val msg = converter.toMsg(value)
    converter.fromMsg(msg).get shouldEqual value
    Await.result(system.terminate(), 3.seconds)
  }
}
