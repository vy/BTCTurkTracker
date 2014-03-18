package com.github.vy.btcturktracker

import com.fasterxml.jackson.databind.{ObjectMapper => _ObjectMapper}
import com.fasterxml.jackson.databind.DeserializationFeature._
import com.fasterxml.jackson.module.scala.DefaultScalaModule

object ObjectMapper {

  private val mapper = new _ObjectMapper()
    .registerModule(DefaultScalaModule)
    .configure(FAIL_ON_NULL_FOR_PRIMITIVES, true)
    .configure(FAIL_ON_UNKNOWN_PROPERTIES,  true)
    .configure(FAIL_ON_IGNORED_PROPERTIES,  true)
    .configure(FAIL_ON_INVALID_SUBTYPE,     true)

  def read[T](content: String, clazz: Class[T]): T = mapper.readValue(content, clazz)

}
