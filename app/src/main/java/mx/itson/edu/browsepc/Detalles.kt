package mx.itson.edu.browsepc

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView

class Detalles : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.template_detalles)

        val iv_producto: ImageView = findViewById(R.id.iv_producto)
        val tv_nombre: TextView = findViewById(R.id.tv_nombre)
        val tv_stock: TextView = findViewById(R.id.tv_stock)
        val tv_precio: TextView = findViewById(R.id.tv_precio)

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

        if(bundle != null){
            iv_producto.setImageResource(bundle.getInt("image"))
            tv_nombre.setText(bundle.getString("nombre"))
            tv_stock.setText(bundle.getString("stock"))
            tv_precio.setText(bundle.getString("precio"))
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