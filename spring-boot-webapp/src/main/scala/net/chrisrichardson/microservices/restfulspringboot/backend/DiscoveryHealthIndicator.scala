package net.chrisrichardson.microservices.restfulspringboot.backend

import java.net.URL

import com.netflix.discovery.EurekaClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.actuate.health.{Health, HealthIndicator}

class DiscoveryHealthIndicator(discoveryClient: EurekaClient) extends HealthIndicator{

  @Value("${user_registration_url}")
  var userRegistrationUrl: String = _

  def health() = {
    val virtualHost = new URL(userRegistrationUrl).getHost
    try {
      val instance = discoveryClient.getNextServerFromEureka(virtualHost, false)
      println("HealthCheckInstance=", instance)
      Health.up().withDetail("description", s"$virtualHost discovery").build()
    } catch {
      case e : RuntimeException =>
        Health.down(e).withDetail("description", s"$virtualHost discovery").build()
    }
  }
}
