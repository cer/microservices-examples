package net.chrisrichardson.microservices.restfulspringboot.backend

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule

class ScalaObjectMapper extends ObjectMapper {

  registerModule(DefaultScalaModule)
}