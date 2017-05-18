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
 * a uma entidade cujo estado deve ser salvo no 
 * banco de dados. 
 */
case class Filme(id: Int, titulo: String, diretor: String, anoProducao: Int) 

/**
 * Um DAO para a classe de entidade Filme. 
 */
class FilmeDAO @Inject() (database: Database){
  val parser : RowParser[models.Filme] = Macro.namedParser[models.Filme]
  
  def salvar(filme: Filme) = database.withConnection { implicit connection => 
    val id: Option[Long] = SQL(
        """INSERT INTO TB_FILME(TITULO, DIRETOR, ANO_PRODUCAO) 
            values ({titulo}, {diretor}, {anoProducao})""")
     .on('titulo -> filme.titulo, 
         'diretor -> filme.diretor, 
         'anoProducao -> filme.anoProducao).executeInsert()
  }
  
  def listar = database.withConnection { implicit connection => 
    SQL"SELECT ID, TITULO, DIRETOR, ANO_PRODUCAO AS anoProducao FROM TB_FILME".as(parser.*)
  }

}