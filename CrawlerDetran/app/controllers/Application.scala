package controllers

import br.com.xdevel.crawlerdetran.model.CrawlerJsonProtocol._
import play.api._
import play.api.mvc._
import scala.concurrent.ExecutionContext
import br.com.xdevel.crawlerdetran.integrator._
import ExecutionContext.Implicits.global
import org.joda.time.DateTime
import br.com.xdevel.crawlerdetran.model._
import spray.json._


object Application extends SecuredController {

  def index = Authenticated {
    val model = new CrawlerModel("OCZ8775", "00345508807")




    val json: spray.json.JsValue =

      JsObject(
        "cap02" -> model.tbServico02.toJson,
        "cap03" -> model.tbServico03.toJson,
        "cap04" -> model.tbServico04.toJson,
        "cap10" -> model.tbServico10.toJson
      )


    Ok(json.prettyPrint)
  }
}