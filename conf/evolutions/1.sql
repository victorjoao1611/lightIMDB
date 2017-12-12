# --- !Ups

CREATE TABLE TB_USUARIO (
  ID INTEGER NOT NULL AUTO_INCREMENT, 
  EMAIL VARCHAR(50), 
  SENHA VARCHAR(12),
  PRIMARY KEY (ID)
); 


CREATE TABLE TB_FILME (
  ID INTEGER NOT NULL AUTO_INCREMENT, 
  TITULO VARCHAR(50), 
  DIRETOR VARCHAR(50),
  ANO_PRODUCAO INTEGER(4),
  DESCRICAO VARCHAR(240),
  PRIMARY KEY (ID)
);

CREATE TABLE TB_COMENTARIO_FILME (
  ID INTEGER NOT NULL AUTO_INCREMENT,
  COMENTARIO VARCHAR(140),
  ID_USUARIO INTEGER,
  ID_FILME INTEGER,
  CONSTRAINT FK_USUARIO_COMENTARIO FOREIGN KEY (ID_USUARIO)
  REFERENCES TB_USUARIO(ID),
  CONSTRAINT FK_FILME_COMENTARIO FOREIGN KEY (ID_FILME)
  REFERENCES TB_FILME(ID),
  PRIMARY KEY(ID)
);

# --- !Downs

DROP TABLE User;