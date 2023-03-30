package mx.itson.edu.browsepc

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class Registro : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        val registrar: Button = findViewById(R.id.btnRegistrar)

        registrar.setOnClickListener{
            var intent: Intent = Intent(this, Bienvenida::class.java)
            startActivity(intent)
        }
    }
}