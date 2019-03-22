package com.evolutiongaming.serialization

import scodec.bits.ByteVector

/**
  * provides ability to have compile time check for serialization presence for remote messages
  * and use passed serialization during remoting
  */
sealed trait ToBytesAble extends Product with Serializable {

  def bytes: ByteVector
}

object ToBytesAble {

  type ToBytes[T] = T => ByteVector

  def apply[T](msg: T)(toBytes: ToBytes[T]): ToBytesAble = raw(msg)(toBytes)

  def raw[T](msg: T)(toBytes: ToBytes[T]): ToBytesAble = Raw(msg)(toBytes)

  def bytes[T](bytes: ByteVector): ToBytesAble = Bytes(bytes)


  final case class Raw[T](msg: T)(toBytes: ToBytes[T]) extends ToBytesAble {

    def bytes = toBytes(msg)
  }


  final case class Bytes(bytes: ByteVector) extends ToBytesAble
}