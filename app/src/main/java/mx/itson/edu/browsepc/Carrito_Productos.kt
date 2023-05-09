package mx.itson.edu.browsepc

data class Carrito_Productos(var id_carrito: String,
                             var id_producto: String,
                             var total: Float,
                             var cantidad: Int){
    constructor(id_carrito: String) : this(id_carrito, "", 0f, 0)
    constructor() : this( "", "", 0f, 0)
}
