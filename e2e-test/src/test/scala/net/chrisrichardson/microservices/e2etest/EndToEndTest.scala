package net.chrisrichardson.microservices.e2etest

import org.junit.runner.RunWith
import org.openqa.selenium.WebDriver
import org.scalatest.BeforeAndAfterAll
import org.scalatest.FlatSpec
import org.scalatest.Matchers._
import org.scalatest.concurrent.Eventually._
import org.scalatest.junit.JUnitRunner
import org.scalatest.selenium.Chrome
import org.scalatest.selenium.WebBrowser
import org.scalatest.selenium.WebBrowser.Page
import org.scalatest.selenium.WebBrowser.go

import org.junit.runner.RunWith
import org.scalatest.Matchers._
import org.scalatest.concurrent.Eventually._
import org.scalatest.junit.JUnitRunner
import org.scalatest.selenium.Chrome
import org.scalatest.{BeforeAndAfterAll, FlatSpec}

import scala.concurrent.duration._


@RunWith(classOf[JUnitRunner])
class EndToEndTest extends FlatSpec with Chrome with BeforeAndAfterAll with MyPages {

  val port = "8080"
  val host = System.getenv("DOCKER_HOST_IP")

  val baseUrl = s"http://$host:$port/"

  def goto(path: String) = go to (baseUrl + path)

  val registrationPage = new RegistrationPage(baseUrl)
  val registrationConfirmationPage = new RegistrationConfirmationPage(baseUrl)

  def makeEmailAddress() = s"foo-${System.currentTimeMillis()}@chrisrichardson.net"

  implicit val longWait = PatienceConfig(10 second, 100 milliseconds)

  it should "register a user" in {
    val emailAddress = makeEmailAddress()

    go to registrationPage

    pageTitle should be(registrationPage.title)

    registrationPage.fillAndSubmit(emailAddress, "secret1234")

    eventually {
      pageTitle should be(registrationConfirmationPage.title)
    }(longWait)

    eventually {
      registrationConfirmationPage.registeredEmailAddress should be(emailAddress)
    }(longWait)
  }



  it should "require long passwords" in {
    val emailAddress = makeEmailAddress()

    go to registrationPage

    pageTitle should be(registrationPage.title)

    registrationPage.fillAndSubmit(emailAddress, "secret")

    eventually {
      pageTitle should be(registrationPage.title)
    }(longWait)

  }

  it should "handle duplicate registrations" in {
    val emailAddress = makeEmailAddress()

    go to registrationPage

    pageTitle should be(registrationPage.title)

    registrationPage.fillAndSubmit(emailAddress, "secret1234")

    eventually {
      pageTitle should be(registrationConfirmationPage.title)
    }(longWait)

    go to registrationPage
    pageTitle should be(registrationPage.title)
    registrationPage.fillAndSubmit(emailAddress, "secret1234")
    eventually {
      pageTitle should be(registrationPage.title)
      pageSource should include ("Email address already registered")
    }(longWait)

  }

  override def afterAll() {
    webDriver.quit()
  }

}
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

