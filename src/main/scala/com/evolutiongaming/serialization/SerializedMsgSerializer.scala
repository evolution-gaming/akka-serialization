package com.evolutiongaming.serialization

import java.io.NotSerializableException

import akka.serialization.SerializerWithStringManifest
import scodec.bits.ByteVector
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

  private val codec = (int32 :: utf8_32 :: variableSizeBytes(int32, bytes)).as[SerializedMsg]

  def toBinary(x: SerializedMsg): ByteVector = {
    codec.encode(x).require.bytes
  }

  def fromBinary(bytes: ByteVector): SerializedMsg = {
    codec.decode(bytes.bits).require.value
  }
}
