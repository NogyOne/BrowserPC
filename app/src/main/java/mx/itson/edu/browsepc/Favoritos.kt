package mx.itson.edu.browsepc

import android.annotation.SuppressLint
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
import com.bumptech.glide.Glide
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.recreate
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class Favoritos : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    lateinit var listView: ListView

    var favoritosList = ArrayList<prod>()
    var favsAdapter: FavoritosAdapter? = null

    lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favoritos)

        listView = findViewById(R.id.listView)

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        val fab: FloatingActionButton = findViewById(R.id.fab)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navigationView: NavigationView = findViewById(R.id.nav_view)

        val buscar: Button = findViewById(R.id.btnBuscar)

        toggle = ActionBarDrawerToggle(this,drawerLayout,R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navigationView.setNavigationItemSelectedListener(this)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        bottomNavigationView.selectedItemId = R.id.cart

        buscar.setOnClickListener{
            var intent: Intent = Intent(this, ProductosBusqueda::class.java)
            startActivity(intent)
        }

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
        favsAdapter = FavoritosAdapter(this, favoritosList,this)
        cargarProductos()
    }

    fun cargarProductos(){
        val collectionFavoritos = DbSingleton.getDb().collection("favoritos")

        val query = collectionFavoritos.whereEqualTo("id_usuario", UserSingleton.getUsuario().id)

        query.get().addOnSuccessListener { documents ->

            for(document in documents){
                val docFav = document.getData()
                println(docFav.get("productos"))
                val productosFav = docFav.get("productos") as ArrayList<HashMap<String, Any>> // obtenemos la lista de productos favoritos como ArrayList<HashMap<String, Any>>
                for(producto in productosFav){
                    val id = producto.get("id") as String
                    val imagen = producto.get("imagen") as String
                    val nombre = producto.get("nombre") as String
                    val precio = producto.get("precio").toString().toFloat()
                    val descuento = producto.get("descuento").toString().toInt()
                    val stock = producto.get("stock").toString().toInt()
                    val prod = prod(id, imagen, nombre, precio, descuento, stock)
                    favoritosList.add(prod)
                }
            }

            println(favoritosList)
            listView.adapter = favsAdapter
        }.addOnFailureListener{e ->
            Log.w(ContentValues.TAG, "Error al obtener los documentos", e)
        }
    }

    fun actualizarListaFavoritos() {
        favoritosList.clear() // Limpia la lista actual de favoritos
        cargarProductos() // Vuelve a cargar los productos favoritos desde la base de datos
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

class FavoritosAdapter : BaseAdapter {
    var productos = ArrayList<prod>()
    var contexto: Context? = null
    lateinit var favoritosActivity: Favoritos

    constructor(context: Context, productos: ArrayList<prod>, act: Favoritos) {
        this.productos = productos
        this.contexto = context
        this.favoritosActivity = act
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
        var vista = inflador.inflate(R.layout.activity_productos_agregados, null)

        var imagen: ImageView = vista.findViewById(R.id.iv_foto)
        var nombre: TextView = vista.findViewById(R.id.tv_nombre)
        var precio: TextView = vista.findViewById(R.id.tv_precioDescuento)
        //var stock: TextView = vista.findViewById(R.id.tv_stock)
        val btnDelete: Button = vista.findViewById(R.id.btnDelete)

        Glide.with(contexto!!).load(producto.imagen).into(imagen)

        nombre.setText(producto.nombre)
        precio.setText(producto.precio.toString())
        //stock.setText(producto.stock.toString() )

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

        btnDelete.setOnClickListener{
            val collectionFavoritos = DbSingleton.getDb().collection("favoritos")
            val query = collectionFavoritos.whereEqualTo("id_usuario", UserSingleton.getUsuario().id)

            query.get().addOnSuccessListener { documents ->
                for(document in documents){
                    val docFav = document.data
                    val productos = docFav["productos"] as ArrayList<HashMap<String, Any>>
                    for(prod in productos){
                        if(prod["nombre"] == producto.nombre){
                            productos.remove(prod)
                            break
                        }
                    }
                    document.reference.set(docFav)
                    favoritosActivity.actualizarListaFavoritos()
                }
                Toast.makeText(contexto, "Se ha eliminado el producto de favoritos", Toast.LENGTH_SHORT).show()
                Log.d(ContentValues.TAG, "Se eliminó")
            }
            .addOnFailureListener{ e->
                Toast.makeText(contexto, "Error al eliminar el producto de favoritos", Toast.LENGTH_SHORT).show()
                Log.w(ContentValues.TAG, "Error al obtener los documentos", e)
            }

        }
        return vista
    }
}
