package models

import anorm.SQL
import anorm.SqlQuery
import anorm.RowParser
import anorm.Macro
import anorm.SqlStringInterpolation
import anorm.SqlParser
import play.api.db.DB
import play.api.Play.current
import javax.inject.Inject
import play.api.db.Database
import javax.inject.Singleton

case class Comentario(id : Int, comentario : String, email : String, titulo : String)

class ComentarioDAO @Inject() (database: Database) {
  val parser : RowParser[models.Comentario] = Macro.namedParser[models.Comentario]

  def salvar(comentario : Comentario) = database.withConnection { implicit connection =>
    val id : Option[Long] = SQL(
      """INSERT INTO TB_COMENTARIO_FILME(COMENTARIO, ID_USUARIO, ID_FILME)
      values ({comentario}, (select ID from TB_USUARIO where EMAIL = {email_usuario}),
      (select ID from TB_FILME where TITULO = {titulo_filme}))""").on(
      'comentario -> comentario.comentario,
      'email_usuario -> comentario.email,
      'titulo_filme -> comentario.titulo).executeInsert()
  }

  def listar = database.withConnection { implicit connection =>
    SQL("SELECT TB_COMENTARIO_FILME.*, a.email AS user_email, b.titulo AS filme_titulo FROM TB_COMENTARIO_FILME " +
      "INNER JOIN TB_USUARIO as a ON a.ID = TB_COMENTARIO_FILME.ID_USUARIO INNER JOIN " +
      "TB_FILME as b ON b.ID = TB_COMENTARIO_FILME.ID_FILME").as(parser.*)
  }
  def procurar(id : Int) = database.withConnection { implicit connection =>
    SQL("SELECT TB_COMENTARIO_FILME.*, a.email AS user_email, b.titulo AS filme_titulo FROM TB_COMENTARIO_FILME " +
      "INNER JOIN TB_USUARIO as a ON a.ID = TB_COMENTARIO_FILME.ID_USUARIO INNER JOIN " +
      "TB_FILME as b ON b.ID = TB_COMENTARIO_FILME.ID_FILME WHERE b.ID = {id}").on(
      'id -> id).as(parser.*)
  }
}