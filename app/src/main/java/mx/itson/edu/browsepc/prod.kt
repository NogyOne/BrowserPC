package mx.itson.edu.browsepc

class prod(var id: String = "",
           var imagen: String = "",
           var nombre: String = "",
           var precio: Float = 0f,
           var descuento: Int = 0,
           var stock: Int = 0) {

    constructor(nombre: String, imagen: String, precio: Float, descuento: Int, stock: Int):this("",nombre, imagen, precio, descuento, stock)
    constructor():this("", "","", 0f, 0, 0)

}