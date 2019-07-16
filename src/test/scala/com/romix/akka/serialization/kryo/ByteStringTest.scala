package com.romix.akka.serialization.kryo

import akka.actor.ActorSystem
import akka.serialization.SerializationExtension
import akka.util.ByteString
import com.typesafe.config.ConfigFactory
import org.scalatest.{Matchers, WordSpecLike}

class ByteStringTest extends WordSpecLike with Matchers {
  val system = ActorSystem("example", ConfigFactory.parseString(
    """
    akka {
      actor {
	kryo {
	  trace = true
	  idstrategy = "default"
	  implicit-registration-logging = true
	  post-serialization-transformations = off
	}
        serializers {
          kryo = "com.romix.akka.serialization.kryo.KryoSerializer"
        }
	serialization-bindings {
	  "akka.util.ByteString$ByteString1C" = kryo
	  "akka.util.ByteString" = kryo
	  "scala.collection.immutable.Vector" = kryo
	}
      }
    }
    """))

  val serialization = SerializationExtension(system)

  "ScalaKryo" should {
    def test(obj: AnyRef): Unit = {
      val serializer = serialization.findSerializerFor(obj)
      (serializer.getClass == classOf[KryoSerializer]) should be(true)
      // Check serialization/deserialization
      val serialized = serialization.serialize(obj)
      serialized.isSuccess should be(true)

      val deserialized = serialization.deserialize(serialized.get, obj.getClass)
      deserialized.isSuccess should be(true)
      (deserialized.get == obj) should be(true)
      deserialized.get.getClass.isAssignableFrom(obj.getClass) should be(true)
      obj.getClass.isAssignableFrom(deserialized.get.getClass) should be(true)
    }

    "handle Vectors" in {
      test(Vector("foo"))
    }

    "handle compact ByteStrings" in {
      test(ByteString("foo").compact)
    }
  }
}
