package net.chrisrichardson.microservices.restfulspringboot

import org.springframework.context.annotation.{Bean, Import, Configuration}
import org.springframework.amqp.core.{BindingBuilder, TopicExchange, Queue}
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer
import MessagingNames._
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.client.RestTemplate
import net.chrisrichardson.microservices.restfulspringboot.backend.ScalaObjectMapper
import scala.collection.JavaConversions._
import java.util.concurrent.LinkedBlockingQueue

@Configuration
@Import(Array(classOf[UserRegistrationConfiguration]))
class UserRegistrationTestConfiguration {

  @Bean
  def queue() = new Queue(queueName, false)

  @Bean
  def binding(queue: Queue, exchange: TopicExchange) = BindingBuilder.bind(queue).to(exchange).`with`(queueName)

  @Bean
  def container(connectionFactory: ConnectionFactory, listenerAdapter: MessageListenerAdapter) = {
    val container = new SimpleMessageListenerContainer()
    container.setConnectionFactory(connectionFactory)
    container.setQueueNames(queueName)
    container.setMessageListener(listenerAdapter)
    container
  }

  @Bean
  def receiver() = new Receiver()


  @Bean
  def listenerAdapter(receiver: Receiver) = new MessageListenerAdapter(receiver, "receiveMessage")

  @Bean
  def restTemplate(scalaObjectMapper: ScalaObjectMapper) = {
    val restTemplate = new RestTemplate()
    restTemplate.getMessageConverters foreach {
      case mc: MappingJackson2HttpMessageConverter =>
        mc.setObjectMapper(scalaObjectMapper)
      case _ =>
    }
    restTemplate
  }
}

class Receiver {

  private val messages = new LinkedBlockingQueue[String]()

  def receiveMessage(message: Array[Byte]) {
    messages.add(new String(message))
  }

  def getMessages = messages.toArray(Array[String]())

}

