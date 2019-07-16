package com.romix.scala.serialization.kryo

import java.util.{ HashMap, Random, TreeMap }
import java.util.concurrent.ConcurrentHashMap

import org.apache.tinkerpop.shaded.kryo.Kryo
import org.apache.tinkerpop.shaded.kryo.io.{ Input, Output }
import org.apache.tinkerpop.shaded.kryo.serializers._

class UnitSerializerTest extends SpecCase {

  "Kryo" should "roundtrip unit " in {
    kryo.setRegistrationRequired(true)
    kryo.addDefaultSerializer(classOf[scala.runtime.BoxedUnit], classOf[ScalaUnitSerializer])
    kryo.register(classOf[scala.runtime.BoxedUnit], 50)
    roundTrip(())
  }

  it should "roundtrip boxedUnit " in {
    kryo.setRegistrationRequired(true)
    kryo.addDefaultSerializer(classOf[scala.runtime.BoxedUnit], classOf[ScalaUnitSerializer])
    kryo.register(classOf[scala.runtime.BoxedUnit], 50)
    roundTrip(scala.runtime.BoxedUnit.UNIT)
  }

}
