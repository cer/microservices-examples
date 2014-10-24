package net.chrisrichardson.microservices.restfulspringboot

import org.springframework.context.annotation.{Bean, Import, Configuration}
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.client.RestTemplate
import net.chrisrichardson.microservices.restfulspringboot.backend.ScalaObjectMapper
import scala.collection.JavaConversions._
import java.util.concurrent.LinkedBlockingQueue

@Configuration
@Import(Array(classOf[UserRegistrationConfiguration]))
class UserRegistrationTestConfiguration {

}


