package net.chrisrichardson.microservices.restfulspringboot.dustview

import javax.script.{Invocable, ScriptEngineManager}
import java.io.{File, InputStreamReader}

import org.slf4j.LoggerFactory
import org.springframework.util.Assert

import scala.concurrent.{Await, Promise}
import org.apache.commons.io.{IOUtils, FileUtils}
import java.util.concurrent.ConcurrentHashMap
import scala.concurrent.duration._

class DustTemplateRenderer {

  import DustTemplateRenderer._

  val logger = LoggerFactory.getLogger(getClass)

  logger.debug("Creating new engine!!!")
  val engineManager = new ScriptEngineManager()
  val engine = engineManager.getEngineByName("nashorn")
  Assert.notNull(engine, "Not running Java 8")

  engine.eval(new InputStreamReader(classOf[DustView].getResourceAsStream("/dustjs/dust-full.js")))
  engine.eval(new InputStreamReader(classOf[DustView].getResourceAsStream("/dustjs/dust-wrapper.js")))
  val dust = engine.eval("dust")
  val myCallback = engine.eval("myDustCallback")
  val outputHolder = engine.eval("outputHolder")
  val invocable = engine.asInstanceOf[Invocable]
  val loadedTemplates = scala.collection.mutable.HashSet[String]()

  compileTemplate("main_template", ClasspathSource("/dustjstemplates/main_template.dust"))

  logger.debug("done creating new engine!!!")


  def compileTemplateFile(templateName : String, file : File) =
    compileTemplate(templateName, FileSource(file))

  def compileTemplate(templateName : String, source : TemplateSource)
  { if (!loadedTemplates.contains(templateName)) {
      logger.debug("loaded template " +  templateName)


      var compiledTemplate = compiledTemplates.get(templateName)
      if (compiledTemplate == null) {
        logger.debug("Compiling template " + templateName)
        val template = source.getString
        compiledTemplate = invocable.invokeMethod(dust, "compile", template, templateName)
        compiledTemplates.putIfAbsent(templateName, compiledTemplate)
        compiledTemplate = compiledTemplates.get(templateName)
      } else {
        logger.debug("Found precompiled template " + templateName)
      }
      invocable.invokeMethod(dust, "loadSource", compiledTemplate)
      loadedTemplates.add(templateName)
    } else {
      logger.debug("template already loaded " + templateName)
    }
  }

  class MyJavaCallback(p : Promise[String]) {

    def done(err : Any, out : Any) {
      if (err == null)
        p.success(out.asInstanceOf[String])
      else {
        p.failure(new RuntimeException(err.toString))
      }
    }
  }


  def renderTemplate(templateName : String, data : Object) = {
    val p = Promise[String]()
    invocable.invokeMethod(outputHolder, "setDone", new MyJavaCallback(p) )
    invocable.invokeMethod(dust, "render", templateName, data, myCallback)

    Await.result(p.future, 500 millis)

  }

  def createObject() = engine.eval("Object.create(Object)")
}

object DustTemplateRenderer {
  trait TemplateSource {
    def getString : String
  }

  case class FileSource(file : File) extends TemplateSource {
    def getString =  FileUtils.readFileToString(file)
  }

  case class ClasspathSource(resource : String) extends TemplateSource {
    def getString =  IOUtils.toString(getClass.getResourceAsStream(resource))
  }


  val compiledTemplates = new ConcurrentHashMap[String, Object]()
}