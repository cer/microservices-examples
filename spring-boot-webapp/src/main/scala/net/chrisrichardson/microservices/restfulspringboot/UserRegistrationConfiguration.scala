package net.chrisrichardson.microservices.restfulspringboot

import net.chrisrichardson.microservices.restfulspringboot.backend.ScalaObjectMapper
import net.chrisrichardson.microservices.restfulspringboot.dustview.DustViewResolver
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.cloud.netflix.eureka.EnableEurekaClient
import org.springframework.context.annotation._
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.client.RestTemplate

import scala.collection.JavaConversions._

@Configuration
@EnableAutoConfiguration
@ComponentScan
@Import(Array(classOf[EurekaClientConfiguration]))
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

  class DummyClass {}

  @Bean
  @Profile(Array("!enableEureka"))
  def restTemplate() = new RestTemplate()

  @Bean
  def restTemplateInitializer(restTemplate : RestTemplate) : DummyClass = {
    restTemplate.getMessageConverters foreach {
      case mc: MappingJackson2HttpMessageConverter =>
        mc.setObjectMapper(scalaObjectMapper())
      case _ =>
    }
    new DummyClass()
  }

}

@Configuration
@EnableEurekaClient
@Profile(Array("enableEureka"))
class EurekaClientConfiguration {

}

