package mx.itson.edu.browsepc

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView

class ProductosBusqueda : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var listView: ListView
    var productos = ArrayList<prod>()
    var adaptador: AdaptadorProductos? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_productos)

        listView = findViewById(R.id.listView)

        val buscar: Button = findViewById(R.id.btnBuscar)
        val param: EditText = findViewById(R.id.txtBusqueda)

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        val fab: FloatingActionButton = findViewById(R.id.fab)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navigationView: NavigationView = findViewById(R.id.nav_view)

        toggle = ActionBarDrawerToggle(this,drawerLayout,R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navigationView.setNavigationItemSelectedListener(this)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        bottomNavigationView.selectedItemId = R.id.cart

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

        val bundle = intent.extras

        buscar.setOnClickListener{
            productos.clear()
            if(param.text.toString()!=null){
                buscarProductos(param.text.toString())
            }
            /*var intent: Intent = Intent(this, ProductosBusqueda::class.java)
            startActivity(intent)*/
        }

        adaptador = AdaptadorProductos(this, productos)
        buscarProductos(bundle?.getString("parametro").toString())

        listView.adapter = adaptador

    }
    fun buscarProductos(parametro: String){
        println("parametro: " + parametro)

        val collectionProductos = DbSingleton.getDb().collection("productos")
        val query = collectionProductos

        query.get().addOnSuccessListener { documents ->
            println("entro a búsqueda por parámetro")
            for (document in documents) {
                val producto = document.toObject(prod::class.java)
                producto.id = document.id

                val nombreProducto = producto.nombre?.toLowerCase()
                if (parametro.isNotEmpty() && nombreProducto != null && nombreProducto.contains(parametro.toLowerCase())) {
                    productos.add(producto)
                }else if(parametro.isEmpty()){
                    productos.add(producto)
                }
            }
            println(productos)

            if (productos.isEmpty()){
                val alertDialog = AlertDialog.Builder(this)

                    .setTitle("No se encontraron productos")
                    .setMessage("No se encontraron productos con el criterio de búsqueda especificado.")
                    .setPositiveButton("Aceptar", null)
                    .create()
                alertDialog.show()
            }

            listView.adapter = adaptador
        }.addOnFailureListener { e ->
            Log.w(ContentValues.TAG, "Error al obtener los documentos", e)
        }
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
            R.id.nav_report ->{
                startActivity(Intent(applicationContext, ReportarProblema::class.java))
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
                finish()
                true
            }
            R.id.nav_favorites ->{
                startActivity(Intent(applicationContext, Favoritos::class.java))
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
class AdaptadorProductos: BaseAdapter{
    var productos = ArrayList<prod>()
    var contexto: Context? = null

    constructor(contexto: Context, producto: ArrayList<prod>) {
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
        var producto = productos[position]
        var inflador = LayoutInflater.from(contexto)
        var vista = inflador.inflate(R.layout.template_producto_busqueda, null)

        var imagen: ImageView = vista.findViewById(R.id.iv_foto)
        var nombre: TextView = vista.findViewById(R.id.tv_nombre)
        var precio: TextView = vista.findViewById(R.id.tv_precioDescuento)
        var stock: TextView = vista.findViewById(R.id.tv_stock)

        Glide.with(contexto!!).load(producto.imagen).into(imagen)

        nombre.setText(producto.nombre)
        precio.setText(producto.precio.toString())
        stock.setText(producto.stock.toString() )

        vista.setOnClickListener{
            var intent = Intent(contexto, Detalles::class.java)
            intent.putExtra("nombre", producto.nombre)
            intent.putExtra("image", producto.imagen)
            intent.putExtra("precio", producto.precio)
            intent.putExtra("stock", producto.stock)
            contexto!!.startActivity(intent)
        }

        return vista
    }
}