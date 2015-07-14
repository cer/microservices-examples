package net.chrisrichardson.microservices.restfulspringboot

import org.springframework.cloud.netflix.eureka.EnableEurekaClient
import org.springframework.context.annotation._
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.amqp.core.TopicExchange
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import net.chrisrichardson.microservices.restfulspringboot.backend.ScalaObjectMapper

@Configuration
@EnableAutoConfiguration
@ComponentScan
@Import(Array(classOf[EurekaClientConfiguration]))
class UserRegistrationConfiguration {

  import MessagingNames._

  @Bean
  @Primary
  def scalaObjectMapper() = new ScalaObjectMapper

  @Bean
  def rabbitTemplate(connectionFactory : ConnectionFactory) = {
    val template = new RabbitTemplate(connectionFactory)
    val jsonConverter = new Jackson2JsonMessageConverter
    jsonConverter.setJsonObjectMapper(scalaObjectMapper())
    template.setMessageConverter(jsonConverter)
    template
  }

  @Bean
  def userRegistrationsExchange() = new TopicExchange(exchangeName)


}

@Configuration
@EnableEurekaClient
@Profile(Array("enableEureka"))
class EurekaClientConfiguration {

}

object MessagingNames {
  val queueName = "user-registration"
  val routingKey = queueName
  val exchangeName = "user-registrations"

}
