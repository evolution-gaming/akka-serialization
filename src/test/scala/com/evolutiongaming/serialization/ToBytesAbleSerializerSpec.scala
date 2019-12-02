package com.evolutiongaming.serialization

import scodec.bits.ByteVector
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class ToBytesAbleSerializerSpec extends AnyFunSuite with Matchers {

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