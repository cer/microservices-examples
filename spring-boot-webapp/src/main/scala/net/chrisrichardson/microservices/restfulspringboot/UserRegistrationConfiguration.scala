package net.chrisrichardson.microservices.restfulspringboot

import com.netflix.discovery.EurekaClient
import net.chrisrichardson.microservices.restfulspringboot.backend.{DiscoveryHealthIndicator, ScalaObjectMapper}
import net.chrisrichardson.microservices.restfulspringboot.dustview.DustViewResolver
import org.springframework.boot.actuate.health.HealthIndicator
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker
import org.springframework.cloud.client.loadbalancer.LoadBalanced
import org.springframework.cloud.netflix.eureka.EnableEurekaClient
import org.springframework.context.annotation._
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.client.RestTemplate
import org.springframework.cloud.sleuth.sampler.AlwaysSampler
import org.springframework.cloud.sleuth.Sampler

import scala.collection.JavaConversions._

@SpringBootApplication
@Import(Array(classOf[EurekaClientConfiguration], classOf[NonEurekaClientConfiguration]))
@EnableCircuitBreaker
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
  def sampler() : Sampler = new AlwaysSampler()
  

}

@Configuration
@Profile(Array("!enableEureka"))
class NonEurekaClientConfiguration {

  @Bean
  def restTemplate(scalaObjectMapper : ScalaObjectMapper) : RestTemplate = {
    val restTemplate = new RestTemplate()
    restTemplate.getMessageConverters foreach {
      case mc: MappingJackson2HttpMessageConverter =>
        mc.setObjectMapper(scalaObjectMapper)
      case _ =>
    }
    restTemplate
  }
}

@Configuration
@EnableEurekaClient
@Profile(Array("enableEureka"))
class EurekaClientConfiguration {

  @Bean
  @LoadBalanced
  def restTemplate(scalaObjectMapper : ScalaObjectMapper) : RestTemplate = {
    val restTemplate = new RestTemplate()
    restTemplate.getMessageConverters foreach {
      case mc: MappingJackson2HttpMessageConverter =>
        mc.setObjectMapper(scalaObjectMapper)
      case _ =>
    }
    restTemplate
  }

  @Bean
  def discoveryHealthIndicator(discoveryClient : EurekaClient ) : HealthIndicator = new DiscoveryHealthIndicator(discoveryClient)
}
