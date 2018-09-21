package com.evolutiongaming.serialization

import akka.actor.ActorSystem
import akka.serialization.{SerializationExtension, SerializerWithStringManifest}
import org.scalatest.{FunSuite, Matchers}

import scala.concurrent.Await
import scala.concurrent.duration._

class SerializedMsgConverterSpec extends FunSuite with Matchers {

  test("toMsg and fromMsg") {
    val system = ActorSystem(getClass.getSimpleName)
    val converter = SerializedMsgExt(system)
    val serialization = SerializationExtension(system)
    val value = "value"
    val expected = converter.toMsg(value)
    val bytes = serialization.serialize(expected).get
    val serializer = serialization.findSerializerFor(expected).asInstanceOf[SerializerWithStringManifest]
    val manifest = serializer.manifest(expected)
    val identifier = serializer.identifier
    val actual = serialization.deserialize(bytes, identifier, manifest).get.asInstanceOf[SerializedMsg]
    converter.fromMsg(actual).get shouldEqual value
    Await.result(system.terminate(), 3.seconds)
  }
}
