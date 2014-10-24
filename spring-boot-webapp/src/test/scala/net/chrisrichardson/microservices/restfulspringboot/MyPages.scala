package net.chrisrichardson.microservices.restfulspringboot

import org.scalatest.selenium.WebBrowser
import org.openqa.selenium.WebDriver

trait MyPages {

  this: WebBrowser =>

  implicit val webDriver: WebDriver

  class RegistrationPage(baseUrl: String) extends Page {

    val url = baseUrl + "register.html"
    val title = "Registration"

    def fillAndSubmit(emailAddress: String, password: String) {

      emailField("emailAddress").value = emailAddress
      pwdField("password").value = password
      submit()
    }
  }

  //  class registrationconfirmationPage(baseUrl: String)(implicit val webDriver: WebDriver) extends WebBrowser.Page with WebBrowser {
  class RegistrationConfirmationPage(baseUrl: String) extends Page {

    val url = baseUrl + "registrationconfirmation.html"

    val title = "Registration Complete"

    def registeredEmailAddress = find("registeredEmailAddress").get.text

  }

}

