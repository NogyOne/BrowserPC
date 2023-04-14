package mx.itson.edu.browsepc

import android.annotation.SuppressLint
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
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView

class MainProductos: AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    lateinit var toggle: ActionBarDrawerToggle
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

        toggle = ActionBarDrawerToggle(this,drawerLayout,R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navigationView.setNavigationItemSelectedListener(this)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)



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


        val buscar: Button = findViewById(R.id.btnBuscar)

        adapterOfertas = ProductoAdapter(this,ofertasList)
        adapterPerifericos = ProductoAdapter(this, perifericosList)
        var listView: ListView = findViewById(R.id.listView)

        cargarProductos()

        listView.adapter = adapterOfertas

        fab.setOnClickListener{
            var intent: Intent = Intent(this, Carrito::class.java)
            startActivity(intent)
        }

        buscar.setOnClickListener{
            var intent: Intent = Intent(this, ProductosBusqueda::class.java)
            startActivity(intent)
        }
    }

    fun cargarProductos(){
        ofertasList.add(Producto(R.drawable.ejemplo_producto,"PC DE VEGETTA777 Y WILLYREX Y RUBIUSOMG", "$2000", "30 disponibles"))
        ofertasList.add(Producto(R.drawable.ejemplo_procesadorintel,"INTEL PROCESADOR CORE I9-12900KF, S-1700, 5.20GHZ, 8-CORE", "$2000", "30 disponibles"))
        ofertasList.add(Producto(R.drawable.ejemplo_procesodorintel2,"PROCESADOR INTEL CORE I9", "$2000", "30 disponibles"))
        ofertasList.add(Producto(R.drawable.ejemplo_procesadorryzen,"PROCESADOR AMD RYZEN 3", "$2000", "30 disponibles"))
        ofertasList.add(Producto(R.drawable.ejemplo_procesadorryzen2,"PROCESADOR AMD RYZEN", "$2000", "30 disponibles"))
        ofertasList.add(Producto(R.drawable.ejemplo_producto,"Gabinete1", "$2000", "30 disponibles"))
        ofertasList.add(Producto(R.drawable.ejemplo_producto,"Gabinete2", "$2000", "30 disponibles"))
        ofertasList.add(Producto(R.drawable.ejemplo_producto,"Gabinete3", "$2000", "30 disponibles"))

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.nav_logout ->{
                startActivity(Intent(applicationContext, MainActivity::class.java))
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
                finish()
                true
            }
            R.id.nav_attention ->{
                startActivity(Intent(applicationContext, AtencionCliente::class.java))
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
                finish()
                true
            }
            else -> false
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)){
            true
        }
        return super.onOptionsItemSelected(item)
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

    @SuppressLint("ViewHolder")
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

        vista.setOnClickListener{
            var intent = Intent(contexto, Detalles::class.java)
            intent.putExtra("nombre", producto.nombre)
            intent.putExtra("image", producto.image)
            intent.putExtra("precio", producto.precio)
            intent.putExtra("stock", producto.stock)
            contexto!!.startActivity(intent)
        }

        return vista
    }
}