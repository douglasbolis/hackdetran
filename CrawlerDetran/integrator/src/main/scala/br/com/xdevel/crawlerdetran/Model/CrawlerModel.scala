package br.com.xdevel.crawlerdetran.model

import br.com.xdevel.crawlerdetran.integrator.Crawler
import org.joda.time.DateTime

/**
 * Created by clayton on 22/06/15.
 */
class CrawlerModel (placa: String,renavam: String) {

    private val crawler = new Crawler(placa,renavam)
    val tbServico02 = _getProp
    val tbServicos03 = _getDebitos
    val tbServicos04 = _getInfracoes
    val tbServicos10 = _getPnalidades


  private def _getProp : List[TField] = crawler.fieldsdiv02
  private def _getDebitos : List[TbServico03] = crawler.fieldsdiv03
  private def _getInfracoes : List[TbServico0410] = crawler.fieldsdiv04
  private def _getPnalidades : List[TbServico0410] = crawler.fieldsdiv10
}




//utillizar depois na persistencia
class TbServico02 (
  var placa : String,
  var renavam : String,
  var placaAnterior : String,
  var tipo : String,
  var categoria: String,
  var especie: String,
  var lugares : String,
  var marcaModelo: String,
  var fabricacaoModelo: String,
  var potencia: String,
  var combustivel: String,
  var cor: String,
  var carroceria: String,
  var nomeProprietario: String,
  var recadastradoDetran: String,
  var proprietarioAnterior: String,
  var origemDadosVeiculo: String,
  var municipioEmplacamento: String,
  var licenciadoAte: String,
  var adquirido: String,
  var situacao: String,
  var restricaoaVenda: String,
  var infoContratoAditivo: String,
  var infoPendenteFinanceiro: String,
  var indicativoClonagem: String,
  var impedimentos: String,
  var averbacaoJudicial: String
)



class TField (var  label: String, var value: String)


//debitos
class TbServico03 (
  var numAuto: String,
  var vencimento: DateTime,
  var valorNominal:	BigDecimal,
  var valorCorrigido:	BigDecimal,
  var valorDesconto:		BigDecimal,
  var valorJuros:		BigDecimal,
  var valorMulta:		BigDecimal,
  var valorAtual:	BigDecimal,
  var signature: String)



//infracoes de autuacao cap04
//penalidades (multas) cap10
class TbServico0410(
  var numAuto: String,
  var descricao: String,
  var localComplemento: String,
  var situacao: String,
  var signature: String)







