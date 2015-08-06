package net.chrisrichardson.microservices.restfulspringboot


import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.google.common.base.Predicate
import org.springframework.context.ApplicationListener
import org.springframework.context.annotation.{Bean, Configuration}
import org.springframework.web.context.request.async.DeferredResult
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors.regex
import springfox.documentation.schema.configuration.ObjectMapperConfigured
import springfox.documentation.service.ApiInfo
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

@Configuration
@EnableSwagger2
class SwaggerConfig {


  @Bean
  def swaggerPaths(): Predicate[String] = regex("/user.*")

  @Bean
  def apiInfo(): ApiInfo = {
    new ApiInfoBuilder()
      .title("Spring Boot rest service")
      .description("user registration API")
      .contact("Chris Richardson")
      .license("Apache License, Version 2.0")
      .version("1.0")
      .build()
  }

  @Bean
  def swaggerSpringMvcPlugin(pathPredicate: Predicate[String], apiInfo: ApiInfo): Docket = {
    new Docket(DocumentationType.SWAGGER_2)
      .genericModelSubstitutes(classOf[DeferredResult[_]])
      .apiInfo(apiInfo)
      .select()
      .paths(pathPredicate)
      .build()
  }

  @Bean
  def objectMapperInitializer = new ApplicationListener[ObjectMapperConfigured] {
    def onApplicationEvent(event: ObjectMapperConfigured) = event.getObjectMapper.registerModule(DefaultScalaModule)
  }

}
