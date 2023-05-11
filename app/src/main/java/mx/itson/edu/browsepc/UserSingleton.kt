package mx.itson.edu.browsepc

object UserSingleton {
    private lateinit var usuario: Usuario

    fun getUsuario(): Usuario = usuario

    fun setUsuario(usuario: Usuario){
        this.usuario = usuario
    }

}