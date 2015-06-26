package br.com.xdevel.crawlerdetran.model

import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat
import spray.json._

import scala.collection.immutable.::

/**
 * Created by clayton on 25/06/15.
 */
object CrawlerJsonProtocol extends DefaultJsonProtocol {
  implicit object TFieldJsonFormat extends RootJsonFormat[TField] {
    def write(c: TField) = JsObject(
      "label" -> JsString(c.label),
      "value" -> JsString(c.value)
    )
    def read(value: JsValue) = {
      value.asJsObject.getFields("label", "value") match {
        case Seq(JsString(label), JsString(value)) =>
          new TField(label,value)
        case _ => throw new DeserializationException("TField expected")
      }
    }
  }



  implicit object TListFieldJsonFormat extends RootJsonFormat[List[TField]] {
    def write(c: List[TField]) = JsArray((for{ el <- c } yield el.toJson).toVector)
    def read(value: JsValue) = {
      value match {
        case x: JsArray =>
          for { el <- x.elements.toList } yield {
            el.toJson.asJsObject.getFields("label", "value") match {
              case Seq(JsString(label), JsString(value)) =>
                new TField(label,value)
              case _ => throw new DeserializationException("TField expected")
            }
          }
        case _ => throw new DeserializationException("TField expected")
      }


    }
  }



    implicit object TbServico03JsonFormat extends RootJsonFormat[TbServico03] {
      def write(c: TbServico03) = JsObject(
        "numAuto" -> JsString(c.numAuto),
        "vencimento" -> JsString(ISODateTimeFormat.dateTime.print(c.vencimento)),
        "valorNominal" -> JsNumber(c.valorNominal),
        "valorCorrigido" -> JsNumber(c.valorCorrigido),
        "valorDesconto" -> JsNumber(c.valorDesconto),
        "valorJuros" -> JsNumber(c.valorDesconto),
        "valorMulta" -> JsNumber(c.valorMulta),
        "valorAtual" -> JsNumber(c.valorAtual),
        "signature" -> JsString(c.signature)
      )

      def read(value: JsValue) = {
        value.asJsObject.getFields("numAuto", "vencimento", "valorNominal", "valorCorrigido", "valorDesconto", "valorJuros", "valorMulta", "valorAtual", "signature") match {
          case Seq(JsString(numAuto), JsString(vencimento), JsNumber(valorNominal), JsNumber(valorCorrigido), JsNumber(valorDesconto), JsNumber(valorJuros), JsNumber(valorMulta), JsNumber(valorAtual), JsString(signature)) =>
            new TbServico03(numAuto,DateTime.parse(vencimento,ISODateTimeFormat.basicDateTime),valorNominal,valorCorrigido ,valorDesconto,valorJuros,valorMulta,valorAtual,signature)
          case _ => throw new DeserializationException("TbServico03 expected")
        }
      }
    }




    implicit object TbServico0410JsonFormat extends RootJsonFormat[TbServico0410] {
      def write(c: TbServico0410) = JsObject(
        "numAuto" -> JsString(c.numAuto),
        "descricao" -> JsString(c.descricao),
        "localComplemento" -> JsString(c.localComplemento),
        "situacao" -> JsString(c.situacao),
        "signature" -> JsString(c.signature)
      )



      def read(value: JsValue) = {
        value.asJsObject.getFields("numAuto","descricao","localComplemento","situacao","signature") match {
          case Seq(JsString(numAuto),JsString(descricao),JsString(localComplemento),JsString(situacao),JsString(signature)) =>
            new TbServico0410(numAuto,descricao,localComplemento,situacao,signature)
          case _ => throw new DeserializationException("TbServico03 expected")
        }
      }
    }
}
