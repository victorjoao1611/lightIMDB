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

case class Avaliacao(id : Int, nota : Float, quantAvaliacoes : Int, titulo : String)

class AvaliacaoDAO @Inject() (database: Database){
  val parser : RowParser[models.Avaliacao] = Macro.namedParser[models.Avaliacao]

  def avaliar(avaliacao: Avaliacao) = database.withConnection { implicit connection =>
    val id: Option[Long] = SQL(
      """IF EXISTS (SELECT TB_AVALIACAO_FILME.*, a.titulo AS filme_titulo FROM TB_AVALICAO_FILME
                    INNER JOIN TB_FILME as a ON a.ID = TB_AVALIACAO_FILME.ID_FILME
                    WHERE a.ID = (select ID from TB_FILME WHERE TITULO = {titulo_filme}))
            UPDATE TB_AVALIACAO_FILME
            SET MEDIA = ((((MEDIA) * (QUANT_AVALIACOES + {quantAvaliacoes})) + {nota}) / (QUANT_AVALIACOES + 1)),
            QUANT_AVALIACOES = ((QUANT_AVALIACOES) + {quantAvaliacoes})
            WHERE a.ID = (select ID from TB_FILME WHERE TITULO = {titulo_filme})
         ELSE
           INSERT INTO TB_AVALIACAO_FILME(MEDIA, QUANT_AVALIACOES, ID_FILME)
           values ({nota}, {quatAvaliacoes}, (select ID from TB_FILME where TITULO = {titulo_filme}))""").on(
         'nota -> avaliacao.nota,
         'quantAvaliacoes -> avaliacao.quantAvaliacoes,
         'titulo_filme -> avaliacao.titulo).executeInsert()
  }

  def procurar(id: Int) = database.withConnection { implicit connection =>
    SQL(
      """SELECT TB_AVALIACAO_FILME.*, a.titulo AS filme_titulo FROM TB_AVALICAO_FILME
         INNER JOIN TB_FILME as a ON a.ID = TB_AVALIACAO_FILME.ID_FILME
         WHERE a.ID = {id}""").on(
      'id -> id
    ).as(parser.*)
  }

}