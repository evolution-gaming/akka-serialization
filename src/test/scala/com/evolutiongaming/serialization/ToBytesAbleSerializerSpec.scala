package com.evolutiongaming.serialization

import org.scalatest.{FunSuite, Matchers}
import scodec.bits.ByteVector

class ToBytesAbleSerializerSpec extends FunSuite with Matchers {

  private val serializer = new ToBytesAbleSerializer()

  test("toBinary & fromBinary for ToBytesAble.Raw") {
    val str = "value"
    val expected = ToBytesAble(str)(str => ByteVector.encodeUtf8(str).toTry.get)
    val actual = toAndFromBinary(expected)
    actual.bytes.decodeUtf8 shouldEqual Right(str)
  }

  test("toBinary & fromBinary for ToBytesAble.Bytes") {
    val str = "value"
    val expected = ToBytesAble(str)(str => ByteVector.encodeUtf8(str).toTry.get)
    val actual = toAndFromBinary(expected)
    actual.bytes.decodeUtf8 shouldEqual Right(str)
  }

  def toAndFromBinary[T <: AnyRef](msg: T): T = {
    val manifest = serializer.manifest(msg)
    val bytes = serializer.toBinary(msg)
    val deserialized = serializer.fromBinary(bytes, manifest)
    deserialized.asInstanceOf[T]
  }
}