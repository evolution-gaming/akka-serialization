package com.evolutiongaming.serialization

import akka.actor.{ExtendedActorSystem, Extension, ExtensionId}
import akka.serialization.{Serialization, SerializationExtension, SerializerWithStringManifest}
import scodec.bits.ByteVector
import scodec.{Codec, codecs}

import scala.annotation.nowarn
import scala.util.Try

/** Object serialized to a binary with the metadata allowing to decode it.
  *
  * The actual fields could be used as the arguments for
  * [[Serialization#deserialize]] call.
  */
final case class SerializedMsg(identifier: Int, manifest: String, bytes: ByteVector)

object SerializedMsg {
  @nowarn("msg=package prefix of the required type")
  implicit val CodecSerializedMsg: Codec[SerializedMsg] = {
    val codec = codecs.int32 :: codecs.utf8_32 :: codecs.variableSizeBytes(codecs.int32, codecs.bytes)
    codec.as[SerializedMsg]
  }
}

/** Provides ability to convert an object to [[SerializedMsg]] and back.
  *
  * This class by itself does not know how to convert `AnyRef` from a binary
  * form and back, and requires an underlying implementation such
  * [[SerializedMsgSerializer]] configured using
  * `akka.actor.serialization-bindings` property in `application.conf` file.
  *
  * Roughly speaking, this class is just a wrapper over [[Serialization]],
  * using [[SerializedMsg]] instead of passing serializer identifier,
  * manifest and the payload to various [[Serialization]] methods.
  *
  * @see
  *   [[Serialization#findSerializerFor]] for more details.
  */
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
      val array = msg.bytes.toArray
      serialization.deserialize(array, msg.identifier, msg.manifest)
    }
  }
}

/** Provides a singleton with [[SerializedMsgConverter]].
  *
  * In other words, it is a simple wrapper over [[SerializationExtension]].
  */
object SerializedMsgExt extends ExtensionId[SerializedMsgConverter] {

  def createExtension(system: ExtendedActorSystem): SerializedMsgConverter = {
    val serialization = SerializationExtension(system)
    SerializedMsgConverter(serialization)
  }
}
