package br.com.xdevel.crawlerdetran.integrator

import br.com.xdevel.crawlerdetran.model._
import org.joda.time.DateTime

import scalaj.http.Http
import scala.collection.JavaConversions._
import org.htmlcleaner.{TagNode, HtmlCleaner}


/**
 * Created by clayton on 13/06/15.
 */





class Crawler(placa : String ,renavam : String) {
  private val resp = Http("http://sitedetran.es.gov.br/consultaveiculoes.asp").timeout(connTimeoutMs = 1000, readTimeoutMs = 10000)
    .postForm(Seq("placa" -> placa, "renavam" -> renavam)).charset("ISO-8859-1").asString.body

  // ahhh detran tu me apronta :D
  private  val hresp = new HtmlCleaner().clean(resp).getAllElementsList(true)








  // infracoes em altuação
  //filterTable03("div_servicos_10",hresp.toList)
  val fieldsdiv04 = getFieldsdiv0410(
    filterTable0410("div_servicos_04",hresp.toList)
  )


  val fieldsdiv10 = getFieldsdiv0410(
    filterTable0410("div_servicos_10",hresp.toList)
  )


  //celnlef8


  // debitos
  val fieldsdiv03 = getFieldsdiv03(filterTable03("div_servicos_03",hresp.toList))

//  fieldsdiv04.flatMap(d=>d.getElementListByName("TABLE", false ).map(d=>d.getElementListByName("tbody", false).map(d=>d.getElementListByName("TR", false)) ))

  // propriedades
  // propriedades gerais
  val fieldsdiv02 =  getFieldsdiv02(filterTable02("div_servicos_02",hresp.toList))


  private  def getFieldsdiv03 (table : List[TagNode]) = {
  //private  def getFieldsdiv03 (table : List[TagNode]) : List[TbServico03] = {

    (for{  d <- table
    }yield {
        def toDateTime (fields : Array[String]) = {
           new DateTime(fields(2).toInt,fields(1).toInt,fields(0).toInt,0,0)
        }

        def toBigDecimal (value : String) : BigDecimal = {
          BigDecimal(value.replace(".","").replace(",","."))
        }

        val cols = d.getElementListByName("td",true)
        new TbServico03(
          cols(0).getAllChildren.last.toString.replaceAll("&nbsp;",""),
          toDateTime(cols(1).getAllChildren.head.toString.split("/")),
          toBigDecimal(cols(2).getAllChildren.head.toString),
          toBigDecimal(cols(3).getAllChildren.head.toString),
          toBigDecimal(cols(4).getAllChildren.head.toString),
          toBigDecimal(cols(5).getAllChildren.head.toString),
          toBigDecimal(cols(6).getAllChildren.head.toString),
          toBigDecimal(cols(7).getAllChildren.head.toString),
          cols(0).getAllChildren.last.toString.replaceAll("&nbsp;","")

        )
      }
      ).toList

  }



  private  def getFieldsdiv0410 (table : List[TagNode]) = {

    (for{  d <- table.tail
    }yield {

        val valuesTop = d.getElementListByAttValue("class","celnlef8",true,true).map(d=>d.getAllChildren)
        val valuesDown = d.getElementListByAttValue("class","celnrig8",true,true).map(d=>d.getAllChildren)

        new   TbServico0410(
          valuesTop(0).mkString,
          valuesTop(1).mkString,
          valuesTop(2).mkString,
          valuesDown(0).mkString,
          (valuesTop(0).mkString + valuesDown(0).mkString)
        )

      }
      ).toList

  }




  private  def getFieldsdiv02 (table : List[TagNode]) : List[TField] = {

    (for{  d <- table
        }yield new
              TField(
                d.getAllChildren.head.toString,
                d.getElementsByName("SPAN",true).map(d=>d.getAllChildren.head.toString).head.toString)
    ).toList

  }



  private def filterTable (divValue : String , el: List[TagNode]  ) : List[TagNode] =  {
    el.filter(d=> d.getName.toUpperCase == "DIV" && d.getAttributes.count(d=>d._1.toUpperCase == "ID" && d._2.toLowerCase.contains(divValue) ) > 0  )
  }



  private def filterTable02 (divValue : String , el: List[TagNode]  ) : List[TagNode] =  {
    filterTable(divValue,el)
      .map (d=> d.getAllElementsList(true).filter(d=> d.getName.toUpperCase == "TABLE"))
      .flatten[TagNode].map(d=> toLines(d)).flatten[TagNode]
  }




  private def filterTable03 (divValue : String , el: List[TagNode]  )  : List[TagNode] =  {
    filterTable(divValue,el)
      .map (d=> d.getAllElementsList(true))
      .head.map(d=>d.getElementListByAttValue("id","Integral",false,true)).filter(d=> !d.isEmpty).head
      .map(d=>d.getElementListByAttValue("class","bordaEsquerdaFina bordaAbaixoFina celnlef",true,true)).head.map(d=>d.getParent).toList
  }


  private def filterTable0410 (divValue : String , el: List[_<: TagNode]  )  =  {
    filterTable(divValue,el).head
      .getElementListByName("TABLE",false).head
      .getElementListByName("tbody",false).head
      .getElementListByName("TR",false).toList
  }


  private  def toLines (tbNode: TagNode) : List[TagNode] = {
    tbNode.getAllElementsList(true).filter(d=> d.getName.toUpperCase == "TR")
      .map(d=>d.getAllElementsList(true).filter(e=>e.getName.toUpperCase == "TD")).flatten[TagNode].toList
  }




}
