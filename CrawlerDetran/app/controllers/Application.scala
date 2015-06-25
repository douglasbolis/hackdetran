package controllers

import br.com.xdevel.crawlerdetran.model.CrawlerModel
import play.api._
import play.api.mvc._
import scala.concurrent.ExecutionContext
import br.com.xdevel.crawlerdetran.integrator._
import ExecutionContext.Implicits.global

object Application extends Controller {

  def index = Action {
    val model = new CrawlerModel("OCZ8775","00345508807")

    Ok(views.html.index("Feito"))
  }

}