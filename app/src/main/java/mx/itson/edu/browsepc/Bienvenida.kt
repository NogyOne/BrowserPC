package mx.itson.edu.browsepc

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class Bienvenida : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bienvenida)

        val empezar: Button = findViewById(R.id.btnEmpezar)
        val username: TextView = findViewById(R.id.txtUsername)
        val bundle = intent.extras

        if(bundle != null){
            username.setText(bundle.getString("username"))
        }

        empezar.setOnClickListener{
            var intent: Intent = Intent(this, MainProductos::class.java)
            startActivity(intent)
        }

    }
}