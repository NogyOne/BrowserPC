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
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView

class Carrito : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    lateinit var listView: ListView

    var carritoList = ArrayList<prod>()
    var carritoAdapter: CarritoAdapter? = null

    lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carrito)

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

        fab.isSelected = true
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

        carritoAdapter = CarritoAdapter(this, carritoList)
        cargarProductos()
    }


    fun cargarProductos(){
        val collectionCarrito = DbSingleton.getDb().collection("carrito")
        val query = collectionCarrito.whereEqualTo("id_usuario", UserSingleton.getUsuario().id)
        var total: Float = 0f

        query.get().addOnSuccessListener { documents ->
            val tv_total: TextView = findViewById(R.id.tv_total)
            for(document in documents){
                val docCart = document.getData()
                println(docCart.get("carritoProducto"))
                val prodCarrito = docCart.get("carritoProducto") as ArrayList<HashMap<String, Any>>

                val tv_num: TextView = findViewById(R.id.tv_numItems)

                tv_num.setText(prodCarrito.size.toString())

                for(producto in prodCarrito){
                    val id = producto.get("id") as String
                    val imagen = producto.get("imagen") as String
                    val nombre = producto.get("nombre") as String
                    val precio = producto.get("precio").toString().toFloat()
                    val descuento = producto.get("descuento").toString().toInt()
                    val stock = producto.get("stock").toString().toInt()
                    val prod = prod(id, imagen, nombre, precio, descuento, stock)

                    total += precio
                    carritoList.add(prod)
                }
            }
            tv_total.setText(total.toString())
            listView.adapter = carritoAdapter
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

class CarritoAdapter : BaseAdapter {
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
            val collectionCarrito = DbSingleton.getDb().collection("carrito")
            val query = collectionCarrito.whereEqualTo("id_usuario", UserSingleton.getUsuario().id)

            query.get().addOnSuccessListener { documents ->
                for(document in documents){
                    val docFav = document.data
                    val productos = docFav["carritoProducto"] as ArrayList<HashMap<String, Any>>
                    for(prod in productos){
                        if(prod["id"] == producto.id.toString()){
                            productos.remove(prod)
                            break
                        }
                    }
                    document.reference.set(docFav)
                }
                Toast.makeText(contexto, "Se ha eliminado el producto del carrito", Toast.LENGTH_SHORT).show()
                Log.d(ContentValues.TAG, "Se eliminó")
            }
                .addOnFailureListener{ e->
                    Toast.makeText(contexto, "Error al eliminar el producto del carrito", Toast.LENGTH_SHORT).show()
                    Log.w(ContentValues.TAG, "Error al obtener los documentos", e)
                }
        }


        return vista
    }
}