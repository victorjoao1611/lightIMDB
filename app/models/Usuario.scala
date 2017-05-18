package models

case class Usuario(email: String, senha: String)

object Usuario {
  var products = Set(
        Usuario("foo", "foo"), 
        Usuario("blah", "blah")
      )
      
   def pesquisaPorEmail(email : String) = products.filter(u => u.email == email).toList(0)  
}