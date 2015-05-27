package net.chrisrichardson.microservices.restfulspringboot.backend

import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import scala.annotation.meta.field

@Document
case class RegisteredUser(id : String, @(Indexed@field)(unique = true) emailAddress : String, password : String)

