package models

import spray.json._

/**
 * Created by clayton on 26/06/15.
 */

object MainJsonProtocol extends DefaultJsonProtocol {

  case class PostVeiculo(email: String, placa: String, renavam: String , id: String)

  case class PostVeiculoEvento (placa: String, title: String , description: String , iddescription : String, isRead: Boolean)

  implicit object TPostVeiculoJsonFormat extends RootJsonFormat[PostVeiculo] {
    def write(c: PostVeiculo) = JsObject(
      "email" -> JsString(c.email),
      "placa" -> JsString(c.placa),
      "renavam" -> JsString(c.renavam),
      "id" -> JsString(c.id)
    )
    def read(value: JsValue) = {
      value.asJsObject.getFields("email", "placa", "renavam","id") match {
        case Seq(
        JsString(email),
        JsString(placa),
        JsString(renavam),
        JsString(id)
        ) =>
          new PostVeiculo(email, placa, renavam, id)
        case _ => throw new DeserializationException("Veiculo expected")
      }
    }
  }





  implicit object TPostVeiculoEventoJsonFormat extends RootJsonFormat[PostVeiculoEvento] {
    def write(c: PostVeiculoEvento) = JsObject(
      "placa" -> JsString(c.placa),
      "title" -> JsString(c.title),
      "description" -> JsString(c.description),
      "iddescription" -> JsString(c.iddescription),
      "isRead" -> JsBoolean(c.isRead)
    )
    def read(value: JsValue) = {
      value.asJsObject.getFields("placa", "title", "description","iddescription", "isRead") match {
        case Seq(
        JsString(placa),
        JsString(title),
        JsString(description),
        JsString(iddescription),
        JsBoolean(isRead)
        ) =>
          new PostVeiculoEvento(placa, title, description, iddescription, isRead)
        case _ => throw new DeserializationException("Veiculo expected")
      }
    }
  }



  case class Login( first_name: String,
                    last_name: String ,
                    gender: String ,
                    link: String ,
                    email: String,
                    locale: String,
                    timeZone: String,
                    network: String,
                    network_id: String)

  implicit object TLoginJsonFormat extends RootJsonFormat[Login] {
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

}
