package net.chrisrichardson.microservices.restfulspringboot.main

import org.springframework.boot.SpringApplication
import net.chrisrichardson.microservices.restfulspringboot.UserRegistrationConfiguration

object UserRegistrationMain extends App {

  SpringApplication.run(classOf[UserRegistrationConfiguration], args: _ *)

}
