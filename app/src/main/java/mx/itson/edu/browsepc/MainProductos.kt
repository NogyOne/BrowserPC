package mx.itson.edu.browsepc

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView

class MainProductos: AppCompatActivity() {

    var adapterOfertas: ProductoAdapter? = null
    var adapterPerifericos: ProductoAdapter? = null
    var ofertasList = ArrayList<Producto>()
    var perifericosList = ArrayList<Producto>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_productos)

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        val fab: FloatingActionButton = findViewById(R.id.fab)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navigationView: NavigationView = findViewById(R.id.nav_view)
//        val toolbar: Toolbar = findViewById(R.id.toolbar)

//        setSupportActionBar(toolbar)
//
//        val toggle: ActionBarDrawerToggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
//        drawerLayout.addDrawerListener(toggle);
//        toggle.syncState();

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
//                    startActivity(Intent(applicationContext, SettingsActivity::class.java))
//                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
//                    finish()
//                    return@setOnItemSelectedListener true
                }
                R.id.menu -> {
                    drawerLayout.openDrawer(GravityCompat.START)
                }
            }
            false
        }


        val buscar: Button = findViewById(R.id.btnBuscar)

        adapterOfertas = ProductoAdapter(this,ofertasList)
        adapterPerifericos = ProductoAdapter(this, perifericosList)
        var gridOfertas: GridView = findViewById(R.id.gridOfertas)

        cargarProductos()

        gridOfertas.adapter = adapterOfertas

        buscar.setOnClickListener{
            var intent: Intent = Intent(this, ProductosBusqueda::class.java)
            startActivity(intent)
        }
    }

    fun cargarProductos(){
        ofertasList.add(Producto(R.drawable.ejemplo_producto,"PC DE VEGETTA777 Y WILLYREX Y RUBIUSOMG", "$2000", "30 disponibles"))
        ofertasList.add(Producto(R.drawable.ejemplo_producto,"PC1", "$2000", "30 disponibles"))
        ofertasList.add(Producto(R.drawable.ejemplo_producto,"PC2", "$2000", "30 disponibles"))
        ofertasList.add(Producto(R.drawable.ejemplo_producto,"PC3", "$2000", "30 disponibles"))
        perifericosList.add(Producto(R.drawable.ejemplo_producto,"Gabinete", "$2000", "30 disponibles"))
        perifericosList.add(Producto(R.drawable.ejemplo_producto,"Gabinete1", "$2000", "30 disponibles"))
        perifericosList.add(Producto(R.drawable.ejemplo_producto,"Gabinete2", "$2000", "30 disponibles"))
        perifericosList.add(Producto(R.drawable.ejemplo_producto,"Gabinete3", "$2000", "30 disponibles"))

    }
}

class ProductoAdapter : BaseAdapter {
    var productos = ArrayList<Producto>()
    var contexto: Context? = null

    constructor(context: Context, productos: ArrayList<Producto>) {
        this.productos = productos
        this.contexto = context
    }

    override fun getCount(): Int {
        return productos.size
    }

    override fun getItem(p0: Int): Any {
        return productos[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var producto = productos[p0]
        var inflador = contexto!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var vista = inflador.inflate(R.layout.template_producto, null)

        var imagen: ImageView = vista.findViewById(R.id.iv_foto)
        var nombre: TextView = vista.findViewById(R.id.tv_nombre)
        var precio: TextView = vista.findViewById(R.id.tv_precioDescuento)
        var stock: TextView = vista.findViewById(R.id.tv_stock)

        imagen.setImageResource(producto.image)
        nombre.setText(producto.nombre)
        precio.setText(producto.precio)
        stock.setText(producto.stock)
        return vista
    }
}