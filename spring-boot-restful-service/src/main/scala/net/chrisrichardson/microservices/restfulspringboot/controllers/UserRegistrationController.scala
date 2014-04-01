package net.chrisrichardson.microservices.restfulspringboot.controllers

import org.springframework.web.bind.annotation._
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.amqp.rabbit.core.RabbitTemplate
import net.chrisrichardson.microservices.restfulspringboot.MessagingNames
import net.chrisrichardson.microservices.restfulspringboot.backend.{RegisteredUserRepository, RegisteredUser}
import org.springframework.dao.DuplicateKeyException


@RestController
class UserRegistrationController @Autowired()(registeredUserRepository: RegisteredUserRepository, rabbitTemplate: RabbitTemplate) {

  import MessagingNames._

  @RequestMapping(value = Array("/user"), method = Array(RequestMethod.POST))
  def registerUser(@RequestBody request: RegistrationRequest) = {
    val registeredUser = new RegisteredUser(null, request.emailAddress, request.password)
    registeredUserRepository.save(registeredUser)
    rabbitTemplate.convertAndSend(exchangeName, routingKey, NewRegistrationNotification(registeredUser.id, request.emailAddress, request.password))
    RegistrationResponse(registeredUser.id, request.emailAddress)
  }

  @ResponseStatus(value = HttpStatus.CONFLICT, reason = "duplicate email address")
  @ExceptionHandler(Array(classOf[DuplicateKeyException]))
  def duplicateEmailAddress() {}


}

case class RegistrationRequest(emailAddress: String, password: String)

case class RegistrationResponse(id: String, emailAddress: String)


case class NewRegistrationNotification(id: String, emailAddress: String, password: String)

