package mx.itson.edu.browsepc

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.drawerlayout.widget.DrawerLayout

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val login: Button = findViewById(R.id.btnLogin)
        val crearCuenta: Button = findViewById(R.id.btnCrearCuenta)
        val olvideContrasena: Button = findViewById(R.id.btnForgotPassword)

        login.setOnClickListener{
            var intent: Intent = Intent(this, Bienvenida::class.java)
            startActivity(intent)
        }

        crearCuenta.setOnClickListener{
            var intent: Intent = Intent(this, Registro::class.java)
            startActivity(intent)
        }

        olvideContrasena.setOnClickListener{
            var intent: Intent = Intent(this, RecuperarContrasena::class.java)
            startActivity(intent)
        }
    }
}