package net.chrisrichardson.microservices.restfulspringboot

import java.util.concurrent.TimeUnit

import org.junit.runner.RunWith
import org.scalatest.{BeforeAndAfterAll, FlatSpec}
import org.scalatest.selenium.Firefox
import org.scalatest.junit.JUnitRunner
import org.springframework.boot.SpringApplication
import org.scalatest.Matchers._
import scala.collection.JavaConversions._
import org.scalatest.concurrent.Eventually._
import scala.concurrent.duration._

@RunWith(classOf[JUnitRunner])
class UserRegistrationWebIntegrationTest extends FlatSpec with Firefox with BeforeAndAfterAll with MyPages {

  override def beforeAll {
    val sa = new SpringApplication(classOf[UserRegistrationTestConfiguration])
    // sa.setAdditionalProfiles("test")
    val ctx = sa.run()
  }

  val port = 8080

  val baseUrl = s"http://localhost:${port}/"

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
