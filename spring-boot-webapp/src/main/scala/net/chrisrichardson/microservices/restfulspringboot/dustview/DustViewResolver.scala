package net.chrisrichardson.microservices.restfulspringboot.dustview

import org.springframework.web.servlet.view.AbstractTemplateViewResolver

class DustViewResolver extends AbstractTemplateViewResolver {

  setViewClass(requiredViewClass())

  override def requiredViewClass = classOf[DustView]

}
