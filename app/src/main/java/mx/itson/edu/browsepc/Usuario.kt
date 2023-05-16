package mx.itson.edu.browsepc

data class Usuario(var id: String,
                   var username: String,
                   var email: String,
                   var password: String,
                   var celular: String){
    constructor(username: String, email: String, password: String, celular: String) : this("", username, email,password, celular)
    constructor(username: String,email: String):this("", username, email, "", "")
    constructor(id: String, username: String,email: String):this(id, username, email, "", "")
    constructor() : this( "", "", "", "", "")
}
