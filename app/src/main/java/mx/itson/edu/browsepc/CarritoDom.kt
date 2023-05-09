package mx.itson.edu.browsepc

data class CarritoDom(var id_usuario: String = "",
                      var total: Float = 0f,
                      var carritoProducto: ArrayList<Carrito_Productos> = ArrayList()
) {
    constructor(id_usuario: String) : this(id_usuario, 0f, ArrayList())
    constructor() : this( "", 0f, ArrayList())
}
