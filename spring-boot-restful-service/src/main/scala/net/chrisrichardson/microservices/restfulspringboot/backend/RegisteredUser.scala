package net.chrisrichardson.microservices.restfulspringboot.backend

import org.springframework.data.mongodb.core.index.Indexed
import scala.annotation.meta.field

case class RegisteredUser(id : String, @(Indexed@field)(unique = true) emailAddress : String, password : String)

