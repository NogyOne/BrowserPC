package mx.itson.edu.browsepc

data class Usuario(var username: String,
                   var email: String,
                   var password: String,
                   var celular: String){
    constructor() : this("", "", "","")
}
