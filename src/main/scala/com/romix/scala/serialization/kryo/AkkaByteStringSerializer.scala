/**
 * *****************************************************************************
 * Copyright 2012 Roman Levenstein
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ****************************************************************************
 */

package com.romix.scala.serialization.kryo

import akka.util.ByteString
import org.apache.tinkerpop.shaded.kryo.{ Kryo, Serializer }
import org.apache.tinkerpop.shaded.kryo.io.{ Input, Output }

/**
 * *
 *
 * Generic serializer for traversable collections
 *
 * @author luben
 *
 */
class AkkaByteStringSerializer() extends Serializer[ByteString] {

  override def read(kryo: Kryo, input: Input, typ: Class[ByteString]): ByteString = {
    val len = input.readInt(true)
    ByteString(input.readBytes(len))
  }

  override def write(kryo: Kryo, output: Output, obj: ByteString) = {
    val len = obj.size
    output.writeInt(len, true)
    obj.foreach { output.writeByte }
  }
}

