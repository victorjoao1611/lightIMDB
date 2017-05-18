package controllers

import play.api.mvc._

class UsuarioController extends Controller {
  
  def login(email: String, senha:String) = Action {
    Ok("foo")
  }
}