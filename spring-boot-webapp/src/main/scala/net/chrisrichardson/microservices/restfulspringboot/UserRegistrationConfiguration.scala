package net.chrisrichardson.microservices.restfulspringboot

import org.springframework.context.annotation.{ComponentScan, Primary, Bean, Configuration}
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import net.chrisrichardson.microservices.restfulspringboot.backend.ScalaObjectMapper
import net.chrisrichardson.microservices.restfulspringboot.dustview.DustViewResolver
import org.springframework.web.client.{ResponseErrorHandler, RestTemplate}
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import scala.collection.JavaConversions._
import org.springframework.http.client.ClientHttpResponse

@Configuration
@EnableAutoConfiguration
@ComponentScan
class UserRegistrationConfiguration {


  @Bean
  @Primary
  def scalaObjectMapper() = new ScalaObjectMapper

  @Bean
  def dustViewResolver = {
    val resolver = new DustViewResolver
    resolver.setPrefix("/WEB-INF/views/")
    resolver.setSuffix(".dust")
    resolver
  }

  @Bean
  def restTemplate = {
    val restTemplate = new RestTemplate()
    restTemplate.getMessageConverters foreach {
      case mc: MappingJackson2HttpMessageConverter =>
        mc.setObjectMapper(scalaObjectMapper())
      case _ =>
    }
    restTemplate
  }

}


