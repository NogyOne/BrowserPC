package mx.itson.edu.browsepc

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class RecuperarContrasena : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recuperar_contrasena)

        var continuar: Button = findViewById(R.id.btnContinuar)

        continuar.setOnClickListener{
            var intent: Intent = Intent(this, NuevaContrasena::class.java)
            startActivity(intent)
        }
    }
}