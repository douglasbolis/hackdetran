package controllers


import models.MainJsonProtocol
import MainJsonProtocol.Login
import models.entities.{UserAuth, User}
import lib.SecuredController
import play.api.mvc.{Action, Controller}
import spray.json._
import models.CrawlerContext._

/**
 * Created by clayton on 26/06/15.
 */
object UserController extends SecuredController {
  def login = Action(parse.json) { request =>
    (request.body ).toString.parseJson.convertTo[Login] match {
      case el: Login => {

        try{
          transactional{
            val user = select[User] where(_.email :== el.email)

            if (user.nonEmpty){
              val auth = select[UserAuth]
                .where(u => (u.user :== user.head) :&& (u.network :== el.network) :&& (u.network_id :== el.network_id))

              if (auth.isEmpty) {
                val userAuth = new UserAuth(user.head,el.network,el.network_id)
              }

            }else{
              val newuser = new User(
                el.first_name,
                Some(el.last_name),
                Some(el.gender),
                Some(el.link),
                el.email,
                Some(""),
                Some(el.locale),
                Some(el.timeZone)
              )
              val userAuth = new UserAuth(newuser,el.network,el.network_id)

            }
          }


          Ok

        }catch {
          case e : Exception =>  BadRequest(JsObject( "err" ->  JsString(e.getMessage)).prettyPrint)
        }



      }
      case _ => BadRequest(JsObject( "err" ->  JsString("Invalid format for login")).prettyPrint)
    }


  }



  def get(email: String): Option[User] ={
    transactional{
      val u = select[User] where(_.email :== email)

      if (u.nonEmpty){
        Some(u.head)
      }else{
        Option.empty
      }
    }
  }





}
