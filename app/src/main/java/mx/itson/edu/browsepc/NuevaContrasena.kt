package mx.itson.edu.browsepc

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class NuevaContrasena : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nueva_contrasena)

        var continuar: Button = findViewById(R.id.btnConfirmar)

        continuar.setOnClickListener{
            var intent: Intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}