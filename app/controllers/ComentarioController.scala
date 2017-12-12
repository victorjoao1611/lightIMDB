package controllers

import play.api.mvc._
import models.Comentario
import models.ComentarioDAO
import javax.inject.Inject
import play.api.data._
import play.api.data.Forms._
import javax.inject.Singleton
import play.api.i18n.I18nSupport
import play.api.i18n.MessagesApi
import java.io.File

@Singleton
class ComentarioController @Inject()(dao: ComentarioDAO, val messagesApi: MessagesApi) extends Controller with I18nSupport {

  def listar = Action {
    var comentarios = dao.listar
    Ok(views.html.comentario.listagem(comentarios))
  }

  def procurar(id : Int) = Action {
    var comentarios = dao.procurar(id)
    Ok(views.html.comentario.mostrar(comentarios))
  }

  def novoComentario = Action {
    Ok(views.html.comentario.novoComentario(comentarioForm))
  }

  def novoComentarioSubmissao = Action { implicit request =>
    comentarioForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.comentario.novoComentario(formWithErrors))
      },
      comentario => {
        val novoComentario = Comentario(0, comentario.comentario, comentario.email, comentario.titulo)
        dao.salvar(novoComentario)
        var comentarios = dao.listar
        Redirect(routes.FilmeController.listar)
      }
    )

  }

  val comentarioForm = Form(
    mapping(
      "Comentario"  -> nonEmptyText,
      "Email" -> nonEmptyText,
      "Titulo" -> nonEmptyText
    )(ComentarioVO.apply)(ComentarioVO.unapply)
  )
}

case class ComentarioVO(comentario: String, email: String, titulo: String)