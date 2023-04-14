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
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView

class ProductosBusqueda : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    lateinit var toggle: ActionBarDrawerToggle
    var productos: ArrayList<Producto> = ArrayList<Producto>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_productos)

        val buscar: Button = findViewById(R.id.btnBuscar)
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
        productos.add(Producto(R.drawable.ejemplo_procesadorintel, "INTEL PROCESADOR CORE I9-12900KF, S-1700, 5.20GHZ, 8-CORE", "$35000", "30 DISPONIBLES"))
        productos.add(Producto(R.drawable.ejemplo_producto, "PCERDA", "$4000", "320 DISPONIBLES"))
        productos.add(Producto(R.drawable.ejemplo_procesodorintel2, "Procesador Intel", "$6000", "10 DISPONIBLES"))
        productos.add(Producto(R.drawable.ejemplo_producto, "LA PODEROSA", "$10000", "20 DISPONIBLES"))
        productos.add(Producto(R.drawable.ejemplo_procesadorryzen, "LA PODEROSA", "$10000", "20 DISPONIBLES"))
        productos.add(Producto(R.drawable.ejemplo_procesadorryzen2, "LA PODEROSA", "$10000", "20 DISPONIBLES"))
        productos.add(Producto(R.drawable.ejemplo_producto, "LA PODEROSA", "$10000", "20 DISPONIBLES"))
        productos.add(Producto(R.drawable.ejemplo_producto, "LA PODEROSA", "$10000", "20 DISPONIBLES"))
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.nav_logout ->{
                startActivity(Intent(applicationContext, MainActivity::class.java))
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
        var producto = productos[position]
        var inflador = LayoutInflater.from(contexto)
        var vista = inflador.inflate(R.layout.template_producto_busqueda, null)

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