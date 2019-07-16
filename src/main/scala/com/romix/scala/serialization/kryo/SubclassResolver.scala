package com.romix.scala.serialization.kryo

import scala.collection.mutable

import org.apache.tinkerpop.shaded.kryo.util.DefaultClassResolver
import org.apache.tinkerpop.shaded.kryo.Registration

class SubclassResolver extends DefaultClassResolver {

  /**
   * We don't want to do subclass resolution during the Kryo.register() call, and unfortunately it
   * hits this a lot. So this doesn't get turned on until the KryoSerializer explicitly enables it,
   * at the end of Kryo setup.
   */
  private var enabled = false

  def enable() = enabled = true

  /**
   * Keep track of the Types we've tried to look up and failed, to reduce wasted effort.
   */
  private lazy val unregisteredTypes: mutable.Set[Class[_]] = mutable.Set(classOf[Object])

  /**
   * Given Class clazz, this recursively walks up the reflection tree and collects all of its
   * ancestors, so we can check whether any of them are registered.
   */
  def findRegistered(clazz: Class[_]): Option[Registration] = {
    if (clazz == null || unregisteredTypes.contains(clazz))
      // Hit the top, so give up
      None
    else {
      val reg = classToRegistration.get(clazz)
      if (reg == null) {
        val result =
          findRegistered(clazz.getSuperclass) orElse
            (Option.empty[Registration] /: clazz.getInterfaces) { (res, interf) =>
              res orElse findRegistered(interf)
            }
        if (result.isEmpty) {
          unregisteredTypes += clazz
        }
        result
      } else {
        Some(reg)
      }
    }
  }

  override def getRegistration(tpe: Class[_]): Registration = {
    val found = super.getRegistration(tpe)
    if (enabled && found == null) {
      findRegistered(tpe) match {
        case Some(reg) => {
          // Okay, we've found an ancestor registration. Add that registration for the current type, so
          // it'll be efficient later. (This isn't threadsafe, but a given Kryo instance isn't anyway.)
          classToRegistration.put(tpe, reg)
          reg
        }
        case None => null
      }
    } else
      found
  }
}
