package controllers

import br.com.xdevel.crawlerdetran.model.CrawlerJsonProtocol.TListFieldJsonFormat
import br.com.xdevel.crawlerdetran.model.CrawlerModel
import controllers.UserController._
import models.CrawlerDetranContext._
import models.entities.{VeiculoEvento, VeiculoRegistro, Veiculo, User}
import play.api.mvc.Action
import spray.json._

/**
 * Created by clayton on 26/06/15.
 */
object VeiculoController extends SecuredController{

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




  def getdados(id : String)  = Action { request =>
      transactional{
        val veiculo = select[Veiculo] where(_.id :== id)

        if(veiculo.isEmpty){
          BadRequest(JsObject("err" -> JsString("Veículo não encontrado")))
        }else{
          val registros = (select[VeiculoRegistro] where (_.veiculo :== veiculo))

          if (registros.isEmpty){
            Ok
          }else{

            val reg = registros.head
            val regcap03 : JsValue = if (reg.reg03.isDefined) {reg.reg03.get.parseJson} else {List().toJson}
            val regcap04 : JsValue = if (reg.reg03.isDefined) {reg.reg04.get.parseJson} else {List().toJson}
            val regcap10 : JsValue = if (reg.reg03.isDefined) {reg.reg10.get.parseJson} else {List().toJson}

            val json: spray.json.JsValue =
              JsObject(
                "cap02" -> reg.reg02.parseJson,
                "cap03" -> regcap03,
                "cap04" -> regcap04,
                "cap10" -> regcap10
              )

            Ok(json)

          }


        }
      }


  }


  def atualizadados(id : String)  = Action { request =>
    transactional{
      val veiculo = select[Veiculo] where(_.id :== id)

      if(veiculo.isEmpty){
        BadRequest(JsObject("err" -> JsString("Veículo não encontrado")))
      }else{
        _registraVeiculo(veiculo.head,true)
        Ok
      }
    }


  }


  def get(email: String) = Action { request =>

    try{


      val user = UserController.get(email)


      transactional{

        if (user.nonEmpty){
          val veiculos = select[Veiculo]
            .where(v => v.user :== user.head)

          Ok(veiculos.map(d=> new PostVeiculo(d.user.email,d.placa,d.renavam,d.id)).toJson.prettyPrint)

        }else{
          Ok(List().toJson.prettyPrint)
        }
      }

    }catch {
      case e : Exception =>  BadRequest(JsObject( "err" ->  JsString(e.getMessage)))
    }


  }


  def getEventos(email: String) = Action { request =>

    try{


      val user = UserController.get(email)


      transactional{

        def getEventos (veiculo: Veiculo,l: List[PostVeiculoEvento]) : List[PostVeiculoEvento] =  {
          transactional{
            val saida = select[VeiculoEvento] where(ve =>  (ve.veiculo :== veiculo) :&& (ve.isRead :== false))

            saida.foreach(d=>{
              d.isRead = true
            })

            l++saida.map(d=>{new PostVeiculoEvento(d.veiculo.placa,d.title,d.description,d.iddescription,true)})

          }
        }

        if (user.nonEmpty){
          val veiculos = select[Veiculo]
            .where(v => v.user :== user.head)

          val eventos =  veiculos.map(d=>{getEventos(d,eventos)})

          Ok(eventos.toJson.prettyPrint)

        }else{
          Ok(List().toJson.prettyPrint)
        }
      }

    }catch {
      case e : Exception =>  BadRequest(JsObject( "err" ->  JsString(e.getMessage)))
    }


  }

  def add  = Action(parse.json) { request =>
    (request.body ).as[PostVeiculo] match {
      case el: PostVeiculo => {
        val user = UserController.get(el.email)
        transactional{
          val veiculos = select[Veiculo] where(v => (v.user :== user) :&& (v.placa :== el.placa) :&& (v.renavam :== el.renavam))

          if (veiculos.isEmpty){
            // novo veiculo
            //TODO checar existencia do usuario com try except
            val newveiculo = new Veiculo(user.get,el.placa,el.renavam)
            _registraVeiculo(newveiculo,false)
            Ok
          }else{
            BadRequest(JsObject( "err" ->  JsString("Veículo Existente")))
          }
        }

      }
      case _ => BadRequest(JsObject( "err" ->  JsString("Invalid format for PostVeiculo")))
    }

  }

  def delete (id : String)  = Action { request =>

    def getbyId(id: String) = transactional {
      select[Veiculo] where (_.id :== id)
    }

    val v = getbyId(id)

    if (v.isEmpty) {
      BadRequest(JsObject("err" -> JsString("Veiculo não encontrado")))
    } else {
      transactional {
        v.foreach(d => {
          d.delete
        })
      }
      Ok
    }

  }


  private def _checaAtualizacoes (veiculo : Veiculo, dados : CrawlerModel) : List[PostVeiculoEvento] = {
    //TODO validacao com parser verificando os registros se tem novos
    List()
  }


  private def _registraVeiculo (veiculo : Veiculo, atualiza: Boolean) = {


    val model = new CrawlerModel(veiculo.placa, veiculo.renavam)

    transactional{
      if (atualiza) {
        _checaAtualizacoes(veiculo, model).foreach(d => {
          // adiciona novos eventos para serem notificados via push
          new VeiculoEvento(d.title, d.description, d.iddescription, false)
        })
        (select[VeiculoRegistro] where (_.veiculo :== veiculo)).foreach(d => {
          d.delete
        })
      }

      new VeiculoRegistro(
        veiculo,
        model.tbServico02.toJson.prettyPrint,
        Some(model.tbServico03.toJson.prettyPrint),
        Some(model.tbServico04.toJson.prettyPrint),
        Some(model.tbServico10.toJson.prettyPrint)
      )
    }


//    val json: spray.json.JsValue =
//
//      JsObject(
//        "cap02" -> model.tbServico02.toJson,
//        "cap03" -> model.tbServico03.toJson,
//        "cap04" -> model.tbServico04.toJson,
//        "cap10" -> model.tbServico10.toJson
//      )
  }

}
