package net.chrisrichardson.microservices.restfulspringboot.main

import org.springframework.boot.SpringApplication
import net.chrisrichardson.microservices.restfulspringboot.UserRegistrationConfiguration

object UserRegistrationMain  {

  def main(args: Array[String]) : Unit = SpringApplication.run(classOf[UserRegistrationConfiguration], args :_ *)

}
