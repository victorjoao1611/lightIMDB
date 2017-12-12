package controllers

import play.api.mvc._
import models.Avaliacao
import models.AvaliacaoDAO
import javax.inject.Inject
import play.api.data._
import play.api.data.Forms._
import javax.inject.Singleton
import play.api.i18n.I18nSupport
import play.api.i18n.MessagesApi
import java.io.File
import play.api.data.format.Formats._

@Singleton
class AvaliacaoController @Inject()(dao: AvaliacaoDAO, val messagesApi: MessagesApi) extends Controller with I18nSupport {

  def procurar(id : Int) = Action {
    var avaliacao = dao.procurar(id)
    Redirect(routes.FilmeController.listar)
  }

  def novaAvaliacaoSubmissao = Action { implicit request =>
    avaliacaoForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.index())
      },
      avaliacao => {
        val novoAvaliacao = Avaliacao(0, avaliacao.nota, avaliacao.quantAvalicoes, avaliacao.titulo)
        dao.avaliar(novoAvaliacao)
        Redirect(routes.FilmeController.listar)
      }
    )
  }

  val avaliacaoForm = Form(
    mapping(
      "Nota"  -> of(floatFormat),
      "QuantAvaliacoes" -> number,
      "Titulo" -> nonEmptyText
    )(AvaliacaoVO.apply)(AvaliacaoVO.unapply)
  )
}

case class AvaliacaoVO(nota: Float, quantAvalicoes : Int, titulo: String)