package com.evolutiongaming.serialization

import org.scalatest.{FunSuite, Matchers}
import scodec.bits.ByteVector

class SerializedMsgSerializerSpec extends FunSuite with Matchers {

  test("toBinary & fromBinary for SerializedMsg") {
    val bytes = "bytes"
    val expected = SerializedMsg(1, "manifest", ByteVector.encodeUtf8(bytes).toTry.get)
    val actual = toAndFromBinary(expected)
    actual.identifier shouldEqual actual.identifier
    actual.manifest shouldEqual actual.manifest
    actual.bytes.decodeUtf8 shouldEqual Right(bytes)
  }

  def toAndFromBinary(msg: SerializedMsg) = {
    val bytes = SerializedMsgSerializer.toBinary(msg)
    SerializedMsgSerializer.fromBinary(bytes)
  }
}
