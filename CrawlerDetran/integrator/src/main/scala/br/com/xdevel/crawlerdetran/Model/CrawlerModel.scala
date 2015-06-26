package br.com.xdevel.crawlerdetran.model

import br.com.xdevel.crawlerdetran.integrator.Crawler

import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat
import spray.json._


/**
 * Created by clayton on 22/06/15.
 *
 */
class CrawlerModel (placa: String,renavam: String) {

    private val crawler = new Crawler(placa,renavam)
    val tbServico02 = _getProp
    val tbServico03 = _getDebitos
    val tbServico04 = _getInfracoes
    val tbServico10 = _getPnalidades


  private def _getProp : List[TField] = crawler.fieldsdiv02
  private def _getDebitos : List[TbServico03] = crawler.fieldsdiv03
  private def _getInfracoes : List[TbServico0410] = crawler.fieldsdiv04
  private def _getPnalidades : List[TbServico0410] = crawler.fieldsdiv10
}




//utillizar depois na persistencia
case class TbServico02 (
  placa : String,
  renavam : String,
  placaAnterior : String,
  tipo : String,
  categoria: String,
  especie: String,
  lugares : String,
  marcaModelo: String,
  fabricacaoModelo: String,
  potencia: String,
  combustivel: String,
  cor: String,
  carroceria: String,
  nomeProprietario: String,
  recadastradoDetran: String,
  proprietarioAnterior: String,
  origemDadosVeiculo: String,
  municipioEmplacamento: String,
  licenciadoAte: String,
  adquirido: String,
  situacao: String,
  restricaoaVenda: String,
  infoContratoAditivo: String,
  infoPendenteFinanceiro: String,
  indicativoClonagem: String,
  impedimentos: String,
  averbacaoJudicial: String
)



case class TField (label: String,value: String)


//debitos
case class TbServico03 (
  numAuto: String,
  vencimento: DateTime,
  valorNominal:	BigDecimal,
  valorCorrigido:	BigDecimal,
  valorDesconto:		BigDecimal,
  valorJuros:		BigDecimal,
  valorMulta:		BigDecimal,
  valorAtual:	BigDecimal,
  signature: String)



//infracoes de autuacao cap04
//penalidades (multas) cap10
case class TbServico0410(
  numAuto: String,
  descricao: String,
  localComplemento: String,
  situacao: String,
  signature: String)







