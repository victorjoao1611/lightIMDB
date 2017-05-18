package controllers

import play.api.mvc._
import models.Filme
import models.FilmeDAO
import models.FilmeDAO

class FilmeController extends Controller {
  
  def listagem = Action {
    var filmes = FilmeDAO.listar
    Ok(views.html.filmes.listagem(filmes))
  }
}