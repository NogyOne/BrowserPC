package mx.itson.edu.browsepc

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException

class MainProductos: AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    lateinit var listView: ListView

    lateinit var toggle: ActionBarDrawerToggle
    var adapterOfertas: ProductoAdapter? = null
    var ofertasList = ArrayList<prod>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_productos)

        listView = findViewById(R.id.listView)

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        val fab: FloatingActionButton = findViewById(R.id.fab)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navigationView: NavigationView = findViewById(R.id.nav_view)

        toggle = ActionBarDrawerToggle(this,drawerLayout,R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navigationView.setNavigationItemSelectedListener(this)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

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
        //adapterPerifericos = ProductoAdapter(this, perifericosList)


        cargarProductos()
        println("Lista: " + ofertasList.size)



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
        val collectionProductos = DbSingleton.getDb().collection("productos")

        val productos = ArrayList<prod>()
        collectionProductos.get().addOnSuccessListener { documents ->
            println("entro al addOnSuccessListener ")
            for(document in documents){
                val producto = document.toObject(prod::class.java)
                producto.id = document.id

                ofertasList.add(producto)
            }
            println(ofertasList)
            listView.adapter = adapterOfertas
        }.addOnFailureListener{e ->
            Log.w(TAG, "Error al obtener los documentos", e)
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

class ProductoAdapter : BaseAdapter {
    var productos = ArrayList<prod>()
    var contexto: Context? = null

    constructor(context: Context, productos: ArrayList<prod>) {
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

        Glide.with(contexto!!).load(producto.imagen).into(imagen)

        nombre.setText(producto.nombre)
        precio.setText(producto.precio.toString())
        stock.setText(producto.stock.toString() )

        vista.setOnClickListener{
            var intent = Intent(contexto, Detalles::class.java)
            intent.putExtra("id", producto.id)
            intent.putExtra("nombre", producto.nombre)
            intent.putExtra("image", producto.imagen)
            intent.putExtra("precio", producto.precio)
            intent.putExtra("stock", producto.stock)
            intent.putExtra("descuento", producto.descuento)
            contexto!!.startActivity(intent)
        }

        return vista
    }
}