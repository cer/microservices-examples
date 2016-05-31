package net.chrisrichardson.microservices.restfulspringboot

import org.junit.runner.RunWith
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.web.client.{HttpClientErrorException, RestTemplate}
import org.springframework.http.HttpStatus
import net.chrisrichardson.microservices.restfulspringboot.controllers.{RegistrationResponse, RegistrationRequest}
import net.chrisrichardson.microservices.restfulspringboot.backend.ScalaObjectMapper
import org.springframework.boot.test.{IntegrationTest, SpringApplicationConfiguration}
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.beans.factory.annotation.Autowired
import org.junit.{Assert, Test}
import org.springframework.test.context.web.WebAppConfiguration

@RunWith(classOf[SpringJUnit4ClassRunner])
@SpringApplicationConfiguration(classes = Array(classOf[UserRegistrationTestConfiguration]))
@WebAppConfiguration
@IntegrationTest
class UserRegistrationWebIntegrationTest {

  @Autowired
  var ctx: ConfigurableApplicationContext = _

  @Autowired
  var scalaObjectMapper: ScalaObjectMapper = _

  @Autowired
  var receiver: Receiver = _

  @Autowired
  var restTemplate: RestTemplate = _

  def makeRegistrationRequest(emailAddress : String, password : String) : RegistrationRequest = {
    val r = new RegistrationRequest
    r.emailAddress = emailAddress
    r.password = password
    r
  }

  @Test
  def shouldRegisterUser {
    val emailAddress = System.currentTimeMillis() + "-a-foo@bar.com"
    val request = makeRegistrationRequest(emailAddress, "secret1234")
    val response = restTemplate.postForEntity("http://localhost:8080/user", request, classOf[RegistrationResponse])
    Assert.assertEquals(HttpStatus.OK, response.getStatusCode)
    eventually {
      val receivedMessages = receiver.getMessages
      receivedMessages.find { _ contains emailAddress} match {
        case Some(_) =>
        case None =>
          Assert.fail(receivedMessages.toList + " expected to contain " + emailAddress)
      }
    }
  }

  def eventually(body : => Unit) : Unit =
    for (i <- 1 to 30) {
      try {
        body
        return
      } catch {
        case e : AssertionError if i < 10 =>
          Thread.sleep(100)
      }
    }

  @Test
  def shouldRejectDuplicateUser {
    val emailAddress = System.currentTimeMillis() + "-b-foo@bar.com"
    val request = makeRegistrationRequest(emailAddress, "secret1234")
    val response1 = restTemplate.postForEntity("http://localhost:8080/user", request, classOf[RegistrationResponse])
    Assert.assertEquals(HttpStatus.OK, response1.getStatusCode)
    try {
      restTemplate.postForEntity("http://localhost:8080/user", request, classOf[RegistrationResponse])
      Assert.fail("expected user registration to fail because of duplicate")
    } catch {
      case e: HttpClientErrorException if e.getStatusCode == HttpStatus.CONFLICT =>
    }
  }

  @Test
  def shouldRejectInvalidRequest{
    val request = makeRegistrationRequest(null, null)
    try {
      restTemplate.postForEntity("http://localhost:8080/user", request, classOf[RegistrationResponse])
      Assert.fail("expected user registration to fail because of bad request")
    } catch {
      case e: HttpClientErrorException if e.getStatusCode == HttpStatus.BAD_REQUEST =>
    }
  }

}