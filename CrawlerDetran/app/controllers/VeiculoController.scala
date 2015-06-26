package controllers

import br.com.xdevel.crawlerdetran.model.CrawlerJsonProtocol.TListFieldJsonFormat
import br.com.xdevel.crawlerdetran.model.CrawlerModel
import controllers.UserController._
import models.CrawlerDetranContext._
import models.entities.{VeiculoEvento, VeiculoRegistro, Veiculo, User}
import play.api.mvc.Action
import spray.json._
import br.com.xdevel.crawlerdetran.model.CrawlerJsonProtocol._
import models.MainJsonProtocol._

/**
 * Created by clayton on 26/06/15.
 */
object VeiculoController extends SecuredController{



  def getdados(id : String)  = Action { request =>
      transactional{
        val veiculo = select[Veiculo] where(_.id :== id)

        if(veiculo.isEmpty){
          BadRequest(JsObject("err" -> JsString("Veículo não encontrado")).prettyPrint)
        }else{
          val registros = (select[VeiculoRegistro] where (_.veiculo :== veiculo))

          if (registros.isEmpty){
            Ok
          }else{

            val reg = registros.head
            val regcap03 : JsValue = if (reg.reg03.isDefined) {reg.reg03.get.parseJson} else {JsArray()}
            val regcap04 : JsValue = if (reg.reg03.isDefined) {reg.reg04.get.parseJson} else {JsArray()}
            val regcap10 : JsValue = if (reg.reg03.isDefined) {reg.reg10.get.parseJson} else {JsArray()}

            val json: spray.json.JsValue =
              JsObject(
                "cap02" -> reg.reg02.parseJson,
                "cap03" -> regcap03,
                "cap04" -> regcap04,
                "cap10" -> regcap10
              )

            Ok(json.prettyPrint)

          }


        }
      }


  }


  def atualizadados(id : String)  = Action { request =>
    transactional{
      val veiculo = select[Veiculo] where(_.id :== id)

      if(veiculo.isEmpty){
        BadRequest(JsObject("err" -> JsString("Veículo não encontrado")).prettyPrint)
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

          Ok(JsArray(veiculos.map(d=> new PostVeiculo(d.user.email,d.placa,d.renavam,d.id).toJson).toVector).prettyPrint)

        }else{
          Ok(JsArray().prettyPrint)
        }
      }

    }catch {
      case e : Exception =>  BadRequest(JsObject( "err" ->  JsString(e.getMessage)).prettyPrint)
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

          def eventos : List[PostVeiculoEvento] =  veiculos.flatMap(d=>{getEventos(d,eventos)})

          Ok(JsArray(eventos.map(d=>d.toJson).toVector).prettyPrint)

        }else{
          Ok(JsArray().prettyPrint)
        }
      }

    }catch {
      case e : Exception =>  BadRequest(JsObject( "err" ->  JsString(e.getMessage)).prettyPrint)
    }


  }

  def add  = Action(parse.json) { request =>
    (request.body ) match {
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
            BadRequest(JsObject( "err" ->  JsString("Veículo Existente")).prettyPrint)
          }
        }

      }
      case _ => BadRequest(JsObject( "err" ->  JsString("Invalid format for PostVeiculo")).prettyPrint)
    }

  }

  def delete (id : String)  = Action { request =>

    def getbyId(id: String) = transactional {
      select[Veiculo] where (_.id :== id)
    }

    val v = getbyId(id)

    if (v.isEmpty) {
      BadRequest(JsObject("err" -> JsString("Veiculo não encontrado")).prettyPrint)
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
          new VeiculoEvento(veiculo, d.title, d.description, d.iddescription, false)
        })
        (select[VeiculoRegistro] where (_.veiculo :== veiculo)).foreach(d => {
          d.delete
        })
      }

      new VeiculoRegistro(
        veiculo,
        model.tbServico02.toJson.prettyPrint,
        Some(JsArray(model.tbServico03.map(d=>d.toJson).toVector).prettyPrint),
        Some(JsArray(model.tbServico04.map(d=>d.toJson).toVector).prettyPrint),
        Some(JsArray(model.tbServico10.map(d=>d.toJson).toVector).prettyPrint)
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
