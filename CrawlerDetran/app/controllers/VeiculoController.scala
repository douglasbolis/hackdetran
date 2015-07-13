package controllers

import br.com.xdevel.crawlerdetran.models.CrawlerModel
import models.Serializers._
import models.entities.{User, VeiculoEvento, VeiculoRegistro, Veiculo}
import lib.SecuredController
import play.api.libs.json._
import models.CrawlerContext._

import play.api.mvc._
import play.api.libs.json._



/**
 * Created by clayton on 26/06/15.
 */
object VeiculoController extends SecuredController {
  def getdados(id: String) = Authenticated(parse.json) { request =>

    transactional{
      val veiculo = select[Veiculo] where(_.id :== id)

      if(veiculo.isEmpty){
        BadRequest(Json.obj("err" -> JsString("Veículo não encontrado")))
      }else{
        val registros = (select[VeiculoRegistro] where (_.veiculo :== veiculo))

        if (registros.isEmpty){
          Ok
        }else{

          val reg = registros.head
          val regcap02 : JsValue = Json.parse(reg.reg02)
          val regcap03 : JsValue = if (reg.reg03.isDefined) {Json.arr(reg.reg03.get)} else {Json.arr()}
          val regcap04 : JsValue = if (reg.reg03.isDefined) {Json.arr(reg.reg04.get)} else {Json.arr()}
          val regcap10 : JsValue = if (reg.reg03.isDefined) {Json.arr(reg.reg10.get)} else {Json.arr()}

          val json =
            Json.obj(
              "cap02" -> regcap02,
              "cap03" -> regcap03,
              "cap04" -> regcap04,
              "cap10" -> regcap10
            )

          Ok(json)

        }


      }
    }

  }



  def atualizadados(id : String)  = Authenticated { request =>
    transactional{
      val veiculo = select[Veiculo] where(_.id :== id)

      if(veiculo.isEmpty){
        BadRequest(Json.obj("err" -> JsString("Veículo não encontrado")))
      }else{
        _registraVeiculo(veiculo.head,true)
        Ok
      }
    }


  }


  def list = Authenticated { request =>

    val email = checkHeader(request)
    if (email.isDefined){
      try{


        val user = User.get(email.get)


        transactional{

          if (user.nonEmpty){
            val veiculos = select[Veiculo]
              .where(v => v.user :== user.head)

            Ok(Json.arr(veiculos.map(d=> new PostVeiculo(d.placa,d.renavam,d.id))))

          }else{
            Ok(Json.arr())
          }
        }

      }catch {
        case e : Exception =>  BadRequest(Json.obj( "err" ->  JsString(e.getMessage)))
      }

    } else{

      Ok(Json.arr())
    }



  }


  def getEventos(email: String) = Authenticated { request =>

    try{


      val user = User.get(email)


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

          Ok(Json.arr(eventos))

        }else{
          Ok(Json.arr())
        }
      }

    }catch {
      case e : Exception =>  BadRequest(Json.obj( "err" ->  JsString(e.getMessage)))
    }


  }

  def add  = Authenticated(parse.json) { request =>

    val email = checkHeader(request)
    if (email.isDefined) {
      val postveiculofrm = request.body.validate[PostVeiculo]

      postveiculofrm.fold(
        errors => {
          BadRequest(Json.obj("err" -> JsError.toFlatJson(errors)))
        },
        el => {

          val user = User.get(email.get)
          transactional{
            val veiculos = select[Veiculo] where(v => (v.user :== user) :&& (v.placa :== el.placa) :&& (v.renavam :== el.renavam))

            if (veiculos.isEmpty){
              // novo veiculo
              //TODO checar existencia do usuario com try except
              val newveiculo = new Veiculo(user.get,el.placa,el.renavam)
              _registraVeiculo(newveiculo,false)
              Ok
            }else{
              BadRequest(Json.obj( "err" ->  JsString("Veículo Existente")))
            }
          }


        })
    }else {
      Unauthorized
    }






  }



  def delete (id : String)  = Authenticated { request =>

    def getbyId(id: String) = transactional {
      select[Veiculo] where (_.id :== id)
    }

    val v = getbyId(id)

    if (v.isEmpty) {
      BadRequest(Json.obj("err" -> JsString("Veiculo não encontrado")))
    } else {
      transactional {
        v.foreach(d => {
          (select[VeiculoEvento] where (_.veiculo :== d)).foreach(e=> e.delete)
          (select[VeiculoRegistro] where (_.veiculo :== d)).foreach(e=> e.delete)
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
        model.tbServico02.toString,
        Some(Json.arr(model.tbServico03).toString),
        Some(Json.arr(model.tbServico04).toString),
        Some(Json.arr(model.tbServico10).toString)
      )
    }

  }

}
