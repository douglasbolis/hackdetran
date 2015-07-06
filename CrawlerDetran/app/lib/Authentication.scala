package lib

import models.CrawlerContext._
import models.entities.User
import play.api.Play
import play.api.mvc.{Controller, RequestHeader, Results, Security}

trait Authentication {
  // Define what you want you want your auth header to be
  val AUTH_TOKEN_HEADER = "X-AUTH-TOKEN"

  object Authenticated extends Security.AuthenticatedBuilder(checkHeader(_), onUnauthorized(_))

  def checkHeader(request: RequestHeader): Option[String] = {
    request.headers.get(AUTH_TOKEN_HEADER) flatMap { token =>
      // do a check to see if there is a user and get their name
      if ( Play.current.configuration.getString("application.secret").isDefined && token == Play.current.configuration.getString("application.secret").get){
        Some("root")
      }else{


        if (!token.isEmpty){
          transactional{
            val user = (select[User].where(_.id :== token))

            if (!user.isEmpty)
              Some(user.head.email)
            else
              Option.empty

          }

        }else
          Option.empty

      }
    }
  }

  def onUnauthorized(request: RequestHeader) = {
    // Do something when the user isn't authorized to access a route
    Results.Unauthorized
  }
}

trait SecuredController extends Controller with Authentication
