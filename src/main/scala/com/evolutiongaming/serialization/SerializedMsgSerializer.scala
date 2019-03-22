package com.evolutiongaming.serialization

import java.io.NotSerializableException

import akka.serialization.SerializerWithStringManifest
import scodec.bits.ByteVector
import scodec.codecs
import scodec.codecs._

class SerializedMsgSerializer extends SerializerWithStringManifest {

  private val Manifest = "A"

  def identifier = 1403526138

  def manifest(x: AnyRef): String = x match {
    case _: SerializedMsg => Manifest
    case _                => illegalArgument(s"Cannot serialize message of ${ x.getClass } in ${ getClass.getName }")
  }

  def toBinary(x: AnyRef) = {
    x match {
      case x: SerializedMsg => SerializedMsgSerializer.toBinary(x).toArray
      case _                => illegalArgument(s"Cannot serialize message of ${ x.getClass } in ${ getClass.getName }")
    }
  }

  def fromBinary(bytes: Array[Byte], manifest: String): AnyRef = {
    manifest match {
      case Manifest => SerializedMsgSerializer.fromBinary(ByteVector.view(bytes))
      case _        => notSerializable(s"Cannot deserialize message for manifest $manifest in ${ getClass.getName }")
    }
  }

  private def notSerializable(msg: String) = throw new NotSerializableException(msg)

  private def illegalArgument(msg: String) = throw new IllegalArgumentException(msg)
}

object SerializedMsgSerializer {

  private val codec = codecs.int32 ~ codecs.utf8_32 ~ codecs.int32 ~ codecs.bytes

  def toBinary(x: SerializedMsg): ByteVector = {
    val value = x.identifier ~ x.manifest ~ x.bytes.length.toInt ~ x.bytes
    val attempt = codec.encode(value)
    attempt.require.bytes
  }

  def fromBinary(bytes: ByteVector): SerializedMsg = {
    val attempt = codec.decode(bytes.bits)
    val identifier ~ manifest ~ length ~ bytes1 = attempt.require.value
    val byteVector1 = bytes1.take(length.toLong)
    SerializedMsg(identifier, manifest, byteVector1)
  }
}
