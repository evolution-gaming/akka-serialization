package com.evolutiongaming.serialization

import scodec.bits.ByteVector
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class SerializedMsgSerializerSpec extends AnyFunSuite with Matchers {

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
