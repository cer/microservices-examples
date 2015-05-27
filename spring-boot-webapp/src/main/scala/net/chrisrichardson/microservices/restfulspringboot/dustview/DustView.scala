package net.chrisrichardson.microservices.restfulspringboot.dustview

import org.springframework.web.servlet.view.AbstractTemplateView
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import java.io.File
import scala.collection.JavaConversions._
import org.springframework.validation.BindingResult
import java.util

class DustView extends AbstractTemplateView {

  import DustView._

  // TODO - interesting. Using a thread local but only this class instantiates the particular template.

  override def renderMergedTemplateModel(model: java.util.Map[String, Object], request: HttpServletRequest, response: HttpServletResponse) {

    val templateName = getUrl()
    val file = templateName.substring("/WEB-INF/views/".length)

    dustTemplateRendererHolder.get.compileTemplate(templateName, DustTemplateRenderer.ClasspathSource("/dustjsviews/" + file))


    val output = dustTemplateRendererHolder.get.renderTemplate(templateName, toJSObject(model))
    response.getOutputStream().write(output.getBytes())
  }

  def toJSObject(javaMap: java.util.Map[String, Object]): AnyRef = {
    val jsObject = dustTemplateRendererHolder.get.createObject().asInstanceOf[java.util.Map[String, Object]]
    for ((key, value) <- javaMap) {
      value match {
        case br: BindingResult =>

          if (br.getFieldErrors.nonEmpty) {
            val toMap: Map[String, Object] = br.getFieldErrors.map {
              e => (e.getField, e.getDefaultMessage.asInstanceOf[Object])
            }.toMap
            val fieldErrors = toJSObject(toMap)
            jsObject.put("fieldErrors", fieldErrors)
          }
          if (br.getGlobalErrors.nonEmpty) {
            val globalErrors: java.util.List[String] = br.getGlobalErrors.map {
              e => e.getDefaultMessage
            }.toList
            jsObject.put("globalErrors", globalErrors)
          }

        case _ => jsObject.put(key, value)
      }
    }
    jsObject
  }

}

object DustView {
  val dustTemplateRendererHolder = new ThreadLocal[DustTemplateRenderer] {
    override def initialValue() = {
      new DustTemplateRenderer();
    }
  }

}