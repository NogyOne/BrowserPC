package mx.itson.edu.browsepc

data class FavoritosDom(var id_usuario: String,
                        var productos: ArrayList<prod>){

    constructor(id_usuario: String) : this(id_usuario, productos = ArrayList())
    constructor():this( "", ArrayList())

}
