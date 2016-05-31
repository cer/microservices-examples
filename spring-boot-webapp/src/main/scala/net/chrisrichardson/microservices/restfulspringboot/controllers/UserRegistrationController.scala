package net.chrisrichardson.microservices.restfulspringboot.controllers

import javax.validation.Valid
import javax.validation.constraints.{NotNull, Size}

import net.chrisrichardson.microservices.restfulspringboot.backend._
import org.hibernate.validator.constraints.Email
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation._
import org.springframework.web.servlet.mvc.support.RedirectAttributes

import scala.beans.BeanProperty

@Controller
class UserRegistrationController @Autowired()(registrationService: RegistrationService) {

  @RequestMapping(value = Array("/register.html"), method = Array(RequestMethod.GET))
  def beginRegister = "register"

  @RequestMapping(value = Array("/register.html"), method = Array(RequestMethod.POST))
  def register(@Valid() @ModelAttribute("registration") request: RegistrationRequest, bindingResult: BindingResult,
               redirectAttributes: RedirectAttributes): String = {
    if (bindingResult.getErrorCount != 0)
      "register"
    else
      registrationService.registerUser(request.getEmailAddress, request.getPassword) match {
        case Right(_) =>
          redirectAttributes.addAttribute("emailAddress", request.getEmailAddress)
          "redirect:registrationconfirmation.html"
        case Left(DuplicateRegistrationError) =>
          bindingResult.rejectValue("emailAddress", "duplicate.email.address", "Email address already registered")
          "register"
      }
  }

  @RequestMapping(value = Array("/registrationconfirmation.html"), method = Array(RequestMethod.GET))
  def registrationconfirmation(@RequestParam emailAddress: String, model: Model) = {
    model.addAttribute("emailAddress", emailAddress)
    "registrationconfirmation"
  }

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










