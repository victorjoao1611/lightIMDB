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

/**
 * A definicao da classe Filme, que corresponde 
 * a uma entidade cujo estado deve ser salvo na 
 * base de dados. 
 */
case class Filme(id: Int, titulo: String, diretor: String, ano_producao: Int) 

/**
 * Um DAO para a classe de entidade Filme. 
 */
object FilmeDAO {
  val parser : RowParser[models.Filme] = Macro.namedParser[models.Filme]
  
  def listar  = DB.withConnection { implicit connection => 
    SQL"SELECT * FROM TB_FILME".as(parser.*)
  }

}