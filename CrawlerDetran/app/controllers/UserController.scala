package controllers


import controllers.UserController._
import models.CrawlerDetranContext._
import play.api.mvc.{Action, Controller}
import spray.json._
import models.entities._


/**
 * Created by clayton on 26/06/15.
 */
object UserController extends SecuredController {

  case class Login( first_name: String,
               last_name: String ,
               gender: String ,
               link: String ,
               email: String,
               locale: String,
               timeZone: String,
               network: String,
               network_id: String)

  implicit object TFieldJsonFormat extends RootJsonFormat[Login] {
    def write(c: Login) = JsObject(
      "first_name" -> JsString(c.first_name),
      "last_name" -> JsString(c.last_name),
      "gender" -> JsString(c.gender),
      "link" -> JsString(c.link),
      "email" -> JsString(c.email),
      "locale" -> JsString(c.locale),
      "timeZone" -> JsString(c.timeZone),
      "network" -> JsString(c.network),
      "network_id" -> JsString(c.network_id)
    )
    def read(value: JsValue) = {
      value.asJsObject.getFields("first_name", "last_name", "gender", "link", "email", "locale", "timeZone", "network", "network") match {
        case Seq(JsString(first_name),
                 JsString(last_name),
                 JsString(gender),
                 JsString(link),
                 JsString(email),
                 JsString(locale),
                 JsString(timeZone),
                 JsString(network),
                 JsString(network_id)) =>
          new Login(first_name, last_name, gender, link, email, locale, timeZone, network, network_id)
        case _ => throw new DeserializationException("Login expected")
      }
    }
  }




  def login = Action(parse.json) { request =>
    (request.body \ "login").as[Login] match {
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
              val userAuth = new UserAuth(user.head,el.network,el.network_id)

            }
          }


          Ok

        }catch {
          case e : Exception =>  BadRequest(JsObject( "err" ->  JsString(e.getMessage)))
        }



      }
      case _ => BadRequest(JsObject( "err" ->  JsString("Invalid format for login")))
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
