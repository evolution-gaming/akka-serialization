package com.evolutiongaming.serialization

import akka.actor.{ExtendedActorSystem, Extension, ExtensionId}
import akka.serialization.{Serialization, SerializationExtension, SerializerWithStringManifest}
import scodec.bits.ByteVector
import scodec.{Codec, codecs}

import scala.util.Try

final case class SerializedMsg(identifier: Int, manifest: String, bytes: ByteVector)

object SerializedMsg {

  implicit val CodecSerializedMsg: Codec[SerializedMsg] = {
    val codec = codecs.int32 :: codecs.utf8_32 :: codecs.variableSizeBytes(codecs.int32, codecs.bytes)
    codec.as[SerializedMsg]
  }
}


trait SerializedMsgConverter extends Extension {

  def toMsg(msg: AnyRef): SerializedMsg

  def fromMsg(msg: SerializedMsg): Try[AnyRef]
}

object SerializedMsgConverter {

  def apply(serialization: Serialization): SerializedMsgConverter = new SerializedMsgConverter {

    def toMsg(msg: AnyRef): SerializedMsg = {
      msg match {
        case msg: SerializedMsg => msg
        case _                  =>
          val serializer = serialization.findSerializerFor(msg)
          val array = serializer.toBinary(msg)
          val bytes = ByteVector.view(array)
          val manifest = serializer match {
            case serializer: SerializerWithStringManifest => serializer.manifest(msg)
            case _ if serializer.includeManifest          => msg.getClass.getName
            case _                                        => ""
          }
          SerializedMsg(serializer.identifier, manifest, bytes)
      }
    }

    def fromMsg(msg: SerializedMsg) = {
      import msg._
      val array = bytes.toArray
      if (manifest.isEmpty) serialization.deserialize(array, identifier, None)
      else serialization.deserialize(array, identifier, manifest)
    }
  }
}


object SerializedMsgExt extends ExtensionId[SerializedMsgConverter] {

  def createExtension(system: ExtendedActorSystem): SerializedMsgConverter = {
    val serialization = SerializationExtension(system)
    SerializedMsgConverter(serialization)
  }
}