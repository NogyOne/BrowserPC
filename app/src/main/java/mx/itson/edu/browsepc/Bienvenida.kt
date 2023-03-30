package mx.itson.edu.browsepc

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class Bienvenida : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bienvenida)

        val empezar: Button = findViewById(R.id.btnEmpezar)

        empezar.setOnClickListener{
            var intent: Intent = Intent(this, MainProductos::class.java)
            startActivity(intent)
        }

    }
}