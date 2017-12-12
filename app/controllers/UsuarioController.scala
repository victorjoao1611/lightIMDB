package controllers

import play.api.mvc._
import models.Usuario
import models.UsuarioLogado
import models.UsuarioDAO
import javax.inject.Inject
import play.api.data._
import play.api.data.Forms._
import javax.inject.Singleton
import play.api.i18n.I18nSupport
import play.api.i18n.MessagesApi

@Singleton
class UsuarioController @Inject()(dao: UsuarioDAO, val messagesApi: MessagesApi) extends Controller with I18nSupport {

  def listar = Action {
    var usuarios = dao.listar
    Ok(views.html.usuarios.listagem(usuarios))
  }

  def novoUsuario = Action {
    Ok(views.html.usuarios.novoUsuario(usuarioForm))
  }

  def novoUsuarioSubmissao = Action { implicit request =>
    usuarioForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.usuarios.novoUsuario(formWithErrors))
      },
      usuario => {
        val novoUsuario = Usuario(0, usuario.email, usuario.senha)
        dao.salvar(novoUsuario)
        var usuarios = dao.listar
        Redirect(routes.HomeController.index())
      }
    )
  }

  def loginUsuario = Action {
    Ok(views.html.usuarios.loginUsuario(usuarioForm))
  }
  def logoutUsuario = Action {
    UsuarioLogado.setEmail("")
    UsuarioLogado.setSenha("")
    Ok(views.html.index())
  }

  def loginUsuarioSubmissao = Action { implicit request =>
    usuarioForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.usuarios.loginUsuario(formWithErrors))
      },
      usuario => {
        val usuarioCadastrado = Usuario(0, usuario.email, usuario.senha)
        val dadosUsuario = dao.procurar(usuarioCadastrado)
        Ok(views.html.index2(dadosUsuario))
      }
    )
  }

  val usuarioForm = Form(
    mapping(
      "Email"  -> nonEmptyText,
      "Senha" -> nonEmptyText
    )(UsuarioVO.apply)(UsuarioVO.unapply)
  )
}

case class UsuarioVO(email: String, senha: String)