package controllers


import models.Serializers
import Serializers._
import models.entities.{UserAuth, User}
import lib.SecuredController
import models.CrawlerContext._
import play.api.libs.json._



/**
 * Created by clayton on 26/06/15.
 */
object UserController extends SecuredController {
  def get = Authenticated(parse.json) { request =>
    val loginfrm = request.body.validate[Login]
    loginfrm.fold(
      errors => {
        BadRequest(Json.obj("err" -> JsError.toFlatJson(errors)))
      },
      el => {
        try {
          transactional {
            val user = select[User] where (_.email :== el.email)

            if (user.nonEmpty) {
              val auth = select[UserAuth]
                .where(u => (u.user :== user.head) :&& (u.network :== el.network) :&& (u.network_id :== el.network_id))

              if (auth.isEmpty) {
                val userAuth = new UserAuth(user.head, el.network, el.network_id)
              }

              Ok(Json.obj("id" -> JsString(user.head.id)))

            } else {
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
              val userAuth = new UserAuth(newuser, el.network, el.network_id)

              Ok(Json.obj("id" -> JsString(newuser.id)))

            }
          }
        } catch {
          case e: Exception => BadRequest(Json.obj("err" -> JsString(e.getMessage)))
        }
      })
  }


}





