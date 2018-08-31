package com.evolutiongaming.serialization

import java.nio.ByteBuffer
import java.nio.charset.{Charset, StandardCharsets}

object SerializerHelper {

  val Utf8: Charset = StandardCharsets.UTF_8


  type Bytes = Array[Byte]

  object Bytes {
    val empty: Bytes = Array.empty
  }


  implicit class ByteBufferOps(val self: ByteBuffer) extends AnyVal {

    def readBytes: Bytes = {
      val length = self.getInt()
      if (length == 0) Bytes.empty
      else {
        val bytes = new Bytes(length)
        self.get(bytes)
        bytes
      }
    }

    def writeBytes(bytes: Bytes): Unit = {
      self.putInt(bytes.length)
      if (bytes.nonEmpty) self.put(bytes)
    }

    def readString: String = {
      new String(readBytes, Utf8)
    }

    def writeString(value: String): Unit = {
      val bytes = value.getBytes(Utf8)
      writeBytes(bytes)
    }
  }
}
