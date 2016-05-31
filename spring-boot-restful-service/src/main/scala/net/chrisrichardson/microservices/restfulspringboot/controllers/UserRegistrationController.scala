package net.chrisrichardson.microservices.restfulspringboot.controllers

import javax.validation.constraints.{Size, NotNull}

import org.hibernate.validator.constraints.Email
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation._
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.amqp.rabbit.core.RabbitTemplate
import net.chrisrichardson.microservices.restfulspringboot.MessagingNames
import net.chrisrichardson.microservices.restfulspringboot.backend.{RegisteredUserRepository, RegisteredUser}
import org.springframework.dao.DuplicateKeyException

import scala.beans.BeanProperty


@RestController
class UserRegistrationController @Autowired()(registeredUserRepository: RegisteredUserRepository, rabbitTemplate: RabbitTemplate) {

  import MessagingNames._

  @RequestMapping(value = Array("/user"), method = Array(RequestMethod.POST))
  def registerUser(@Validated @RequestBody request: RegistrationRequest) = {
    val registeredUser = new RegisteredUser(null, request.emailAddress, request.password)
    registeredUserRepository.save(registeredUser)
    rabbitTemplate.convertAndSend(exchangeName, routingKey, NewRegistrationNotification(registeredUser.id, request.emailAddress, request.password))
    RegistrationResponse(registeredUser.id, request.emailAddress)
  }

  @ResponseStatus(value = HttpStatus.CONFLICT, reason = "duplicate email address")
  @ExceptionHandler(Array(classOf[DuplicateKeyException]))
  def duplicateEmailAddress() {}


}

class RegistrationRequest {

  @BeanProperty
  @Email
  @NotNull
  var emailAddress: String = _

  @BeanProperty
  @NotNull
  @Size(min = 8, max = 30)
  var password: String = _

}

case class RegistrationResponse(id: String, emailAddress: String)


case class NewRegistrationNotification(id: String, emailAddress: String, password: String)

