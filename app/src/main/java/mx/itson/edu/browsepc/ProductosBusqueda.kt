package mx.itson.edu.browsepc

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView

class ProductosBusqueda : AppCompatActivity() {

    var productos: ArrayList<Producto> = ArrayList<Producto>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_productos)

        val buscar: Button = findViewById(R.id.btnBuscar)
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        val fab: FloatingActionButton = findViewById(R.id.fab)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navigationView: NavigationView = findViewById(R.id.nav_view)

        bottomNavigationView.selectedItemId = R.id.home

        bottomNavigationView.setOnItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.home -> {
                    startActivity(Intent(applicationContext, MainProductos::class.java))
                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
                    finish()
                    return@setOnItemSelectedListener true
                }
                R.id.cart -> {
                    startActivity(Intent(applicationContext, Carrito::class.java))
                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
                    finish()
                    return@setOnItemSelectedListener true
                }
                R.id.menu -> {
                    drawerLayout.openDrawer(GravityCompat.START)
                }
            }
            false
        }

        fab.setOnClickListener{
            var intent: Intent = Intent(this, Carrito::class.java)
            startActivity(intent)
        }

        buscar.setOnClickListener{
            var intent: Intent = Intent(this, ProductosBusqueda::class.java)
            startActivity(intent)
        }

        agregarProductos()

        var listView: ListView = findViewById(R.id.listView)

        var adaptador: AdaptadorProductos = AdaptadorProductos(this, productos)
        listView.adapter = adaptador

    }

    fun agregarProductos(){
        productos.add(Producto(R.drawable.ejemplo_producto, "Gabinete", "35000", "30"))
        productos.add(Producto(R.drawable.ejemplo_producto, "PCERDA", "4000", "320"))
        productos.add(Producto(R.drawable.ejemplo_producto, "COMPUTADORA", "6000", "10"))
        productos.add(Producto(R.drawable.ejemplo_producto, "LA PODEROSA", "10000", "20"))
        productos.add(Producto(R.drawable.ejemplo_producto, "LA PODEROSA", "10000", "20"))
        productos.add(Producto(R.drawable.ejemplo_producto, "LA PODEROSA", "10000", "20"))
        productos.add(Producto(R.drawable.ejemplo_producto, "LA PODEROSA", "10000", "20"))
        productos.add(Producto(R.drawable.ejemplo_producto, "LA PODEROSA", "10000", "20"))
    }
}

private class AdaptadorProductos: BaseAdapter{
    var productos = ArrayList<Producto>()
    var contexto: Context? = null

    constructor(contexto: Context, producto: ArrayList<Producto>) {
        this.productos = producto
        this.contexto = contexto
    }

    override fun getCount(): Int {
        return productos.size
    }

    override fun getItem(position: Int): Any {
        return productos[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var prod = productos[position]
        var inflador = LayoutInflater.from(contexto)
        var vista = inflador.inflate(R.layout.template_producto_busqueda, null)

        var imagen = vista.findViewById(R.id.iv_foto) as ImageView
        var nombre = vista.findViewById(R.id.tv_nombre) as TextView
        var precio = vista.findViewById(R.id.tv_precioNormal) as TextView

        imagen.setImageResource(prod.image)
        nombre.setText(prod.nombre)
        precio.setText("$${prod.precio}")
        return vista
    }
}