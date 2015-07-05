package models

import br.com.xdevel.crawlerdetran.models.{TbServico02, TbServico0410, TbServico03, TField}
import org.joda.time.DateTime
import org.joda.time.format.{DateTimeFormat, ISODateTimeFormat}
import play.api.libs.json._
import play.api.libs.functional.syntax._


/**
 * Created by clayton on 26/06/15.
 */

object Serializers {

  val pattern = "yyyy-MM-dd'T'HH:mm:ssz"
  implicit val dateFormat =
    Format[DateTime](Reads.jodaDateReads(pattern), Writes.jodaDateWrites(pattern))

  val userDateFormatter = DateTimeFormat.forPattern(pattern)



  case class Login( first_name: String,
                    last_name: String ,
                    gender: String ,
                    link: String ,
                    email: String,
                    locale: String,
                    timeZone: String,
                    network: String,
                    network_id: String)

  implicit val loginWrites: Writes[Login] = (
    (JsPath \ "first_name").write[String] and
      (JsPath \ "last_name").write[String] and
      (JsPath \ "gender").write[String] and
      (JsPath \ "link").write[String] and
      (JsPath \ "email").write[String] and
      (JsPath \ "locale").write[String] and
      (JsPath \ "timeZone").write[String] and
      (JsPath \ "network").write[String] and
      (JsPath \ "network_id").write[String]
    )(unlift(Login.unapply))


  implicit val loginReads: Reads[Login] = (
    (JsPath \ "first_name").read[String] and
      (JsPath \ "last_name").read[String] and
      (JsPath \ "gender").read[String] and
      (JsPath \ "link").read[String] and
      (JsPath \ "email").read[String] and
      (JsPath \ "locale").read[String] and
      (JsPath \ "timeZone").read[String] and
      (JsPath \ "network").read[String] and
      (JsPath \ "network_id").read[String]
    )(Login.apply _)


  case class PostVeiculo(placa: String, renavam: String , id: String)



  implicit val postVeiculoWrites = Json.writes[PostVeiculo]
  implicit val postVeiculoReads = Json.reads[PostVeiculo]
//  implicit val postVeiculoWrites: Writes[PostVeiculo] = (
//      (JsPath \ "placa").write[String] and
//      (JsPath \ "renavam").write[String] and
//      (JsPath \ "id").write[String]
//    )(unlift(PostVeiculo.unapply))
//
//
//  implicit val postVeiculoReads: Reads[PostVeiculo] = (
//      (JsPath \ "placa").read[String] and
//      (JsPath \ "renavam").read[String] and
//      (JsPath \ "id").read[String]
//    )(PostVeiculo.apply _)




  case class PostVeiculoEvento (placa: String, title: String , description: String , iddescription : String, isRead: Boolean)


  implicit val postVeiculoEventoWrites: Writes[PostVeiculoEvento] = (
    (JsPath \ "placa").write[String] and
      (JsPath \ "title").write[String] and
      (JsPath \ "description").write[String] and
      (JsPath \ "iddescription").write[String] and
      (JsPath \ "isRead").write[Boolean]
    )(unlift(PostVeiculoEvento.unapply))


  implicit val postVeiculoEventoReads: Reads[PostVeiculoEvento] = (
    (JsPath \ "placa").read[String] and
      (JsPath \ "title").read[String] and
      (JsPath \ "description").read[String] and
      (JsPath \ "iddescription").read[String] and
      (JsPath \ "isRead").read[Boolean]
    )(PostVeiculoEvento.apply _)




  implicit val TFieldWrites = Json.writes[TField]
  implicit val TFieldReads = Json.reads[TField]

//  implicit val TFieldWrites: Writes[TField] = (
//    (JsPath \ "label").write[String] and
//      (JsPath \ "value").write[String]
//    )(unlift(TField.unapply))
//
//
//  implicit val TFieldReads: Reads[TField] = (
//    (JsPath \ "label").read[String] and
//      (JsPath \ "value").read[String]
//    )(TField.apply _)



  implicit val TbServico03Reads = Json.reads[TbServico03]

//  implicit val TbServico03Reads: Reads[TbServico03] = (
//    (JsPath \ "numAuto").read[String] and
//      (JsPath \ "vencimento").read[String].fmap[DateTime](dt => DateTime.parse(dt,userDateFormatter)) and
//      (JsPath \ "valorNominal").read[BigDecimal] and
//      (JsPath \ "valorCorrigido").read[BigDecimal] and
//      (JsPath \ "valorDesconto").read[BigDecimal] and
//      (JsPath \ "valorJuros").read[BigDecimal] and
//      (JsPath \ "valorMulta").read[BigDecimal] and
//      (JsPath \ "valorAtual").read[BigDecimal] and
//      (JsPath \ "signature").read[String]
//    )(unlift(TbServico03.unapply))


 implicit val TbServico03Writes = Json.writes[TbServico03]

//  implicit val TbServico03Writes: Writes[TbServico03] = (
//    (JsPath \ "numAuto").write[String] and
//      (JsPath \ "vencimento").write[String].contramap[DateTime](dt => userDateFormatter.print(dt)) and
//      (JsPath \ "valorNominal").write[BigDecimal] and
//      (JsPath \ "valorCorrigido").write[BigDecimal] and
//      (JsPath \ "valorDesconto").write[BigDecimal] and
//      (JsPath \ "valorJuros").write[BigDecimal] and
//      (JsPath \ "valorMulta").write[BigDecimal] and
//      (JsPath \ "valorAtual").write[BigDecimal] and
//      (JsPath \ "signature").write[String]
//    )(TbServico03.apply _)



  implicit val TbServico0410Reads = Json.reads[TbServico0410]



//  implicit val TbServico0410Reads: Reads[TbServico0410] = (
//    (JsPath \ "numAuto").read[String] and
//      (JsPath \ "descricao").read[String] and
//      (JsPath \ "localComplemento").read[String] and
//      (JsPath \ "situacao").read[String] and
//      (JsPath \ "signature").read[String]
//    )(unlift(TbServico0410.unapply))


  implicit val TbServico0410Writes = Json.writes[TbServico0410]

//  implicit val TbServico0410Writes: Writes[TbServico0410] = (
//    (JsPath \ "numAuto").write[String] and
//      (JsPath \ "descricao").write[String] and
//      (JsPath \ "localComplemento").write[String] and
//      (JsPath \ "situacao").write[String] and
//      (JsPath \ "signature").write[String]
//    )(TbServico0410.apply _)






}
