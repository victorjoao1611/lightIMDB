package controllers

import play.api.mvc._
import models.Filme
import models.FilmeDAO
import javax.inject.Inject
import play.api.data._
import play.api.data.Forms._
import javax.inject.Singleton
import play.api.i18n.I18nSupport
import play.api.i18n.MessagesApi
import java.io.File

/**
 * A classe que processa as requisicoes do usuario 
 * relacionadas a filmes e interage com o meio de 
 * persitencia. 
 * 
 * Utiliza o mecanismo de injecao de dependencia do 
 * PlayFramework para ter acesso ao DAO FilmeDAO.  
 */
@Singleton
class FilmeController @Inject()(dao: FilmeDAO, val messagesApi: MessagesApi) extends Controller with I18nSupport {
  
  def listar = Action {
    var filmes = dao.listar
    Ok(views.html.filmes.listagem(filmes))
  }

  def procurar(id : Int) = Action {
    var filme = dao.procurar(id)
    Ok(views.html.filmes.mostar(filme))
  }
  
  def novoFilme = Action {
    Ok(views.html.filmes.novoFilme(filmeForm))
  }

  def uploadImagem(id : Int) = Action(parse.multipartFormData) { request =>
    request.body.file("fileUpload").map { image =>
      val imageFilename = image.filename
      val contentType = image.contentType.get
      image.ref.moveTo(new File("./public/images/" + id.toString + ".jpg"))
    }.getOrElse {
      Redirect(routes.FilmeController.listar)
    }
    Redirect(routes.FilmeController.listar)
  }
  
  def novoFilmeSubmissao = Action { implicit request =>
    filmeForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.filmes.novoFilme(formWithErrors))
      },
      filme => {
        val novoFilme = Filme(0, filme.titulo, filme.diretor, filme.ano, filme.descricao)
        dao.salvar(novoFilme)
        var filmes = dao.listar
        Created(views.html.filmes.listagem(filmes))
    }
  )
    
  }
  
  val filmeForm = Form(
    mapping(
      "Titulo"  -> nonEmptyText,
      "Diretor" -> nonEmptyText,
      "Ano" -> number(min=1950, max=2050),
      "Descricao" -> nonEmptyText
    )(FilmeVO.apply)(FilmeVO.unapply)    
  )
}

case class FilmeVO(titulo: String, diretor: String, ano: Int, descricao: String)