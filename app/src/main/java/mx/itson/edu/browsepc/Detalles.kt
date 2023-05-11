package mx.itson.edu.browsepc

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Detalles : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    lateinit var toggle: ActionBarDrawerToggle
    private val db = Firebase.firestore
    private val collectionFavoritos = db.collection("favoritos")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.template_detalles)

        val iv_producto: ImageView = findViewById(R.id.iv_producto)
        val tv_nombre: TextView = findViewById(R.id.tv_nombre)
        val tv_stock: TextView = findViewById(R.id.tv_stock)
        val tv_precio: TextView = findViewById(R.id.tv_precio)
        val btnFavoritos: Button = findViewById(R.id.btnFavoritos)


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

        btnFavoritos.setOnClickListener{
            val user = UserSingleton.getUsuario()
            var producto = prod(bundle?.getString("id").toString(), bundle?.getString("image").toString(), tv_nombre.text.toString(), tv_precio.text.toString().toFloat())
            producto.stock = tv_stock.text.toString().toInt()

            println("Producto con id: ${producto.id}")
            agregarFavoritos(producto)
        }

        if(bundle != null){
            Glide.with(this).load(bundle.getString("image")).into(iv_producto)
            tv_nombre.setText(bundle.getString("nombre"))
            tv_stock.setText(bundle.getInt("stock").toString())
            tv_precio.setText(bundle.getFloat("precio").toString())

        }
    }

    fun agregarFavoritos(producto: prod){
        var favoritos = ArrayList<prod>()
        favoritos.add(producto)

        val query = collectionFavoritos.whereEqualTo("id_usuario", UserSingleton.getUsuario().id)
        query.get().addOnSuccessListener { documents ->
            if(documents.size() == 1){
                for(document in documents){
                    val docRef = collectionFavoritos.document(document.id) //Referencia al documento favoritos
                    docRef.update("productos", FieldValue.arrayUnion(producto))
                        .addOnSuccessListener {
                            Log.d(ContentValues.TAG, "Producto agregado a favoritos del usuario: ${UserSingleton.getUsuario().id}")
                            Toast.makeText(baseContext, "Producto agregado a favoritos", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener{ e->
                            Log.w(ContentValues.TAG, "Error al agregar a favoritos ", e)
                            Toast.makeText(baseContext, "Error al agregar a favoritos, intenta de nuevo", Toast.LENGTH_SHORT).show()
                        }
                }
            }
        }
            .addOnFailureListener{ e ->
                Log.w(ContentValues.TAG, "Error al cargar el producto ${producto.id} a Favoritos", e)
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