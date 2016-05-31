package net.chrisrichardson.microservices.restfulspringboot.backend

trait RegistrationService {

  def registerUser(emailAddress: String, password : String) : Either[RegistrationError, String]

}
