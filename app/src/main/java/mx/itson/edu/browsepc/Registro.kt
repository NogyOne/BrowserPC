package mx.itson.edu.browsepc

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class Registro : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        val registrar: Button = findViewById(R.id.btnRegistrar)
        val userName: TextView = findViewById(R.id.txtUsername)
        val contrasena: TextView = findViewById(R.id.txtContrasena)
        val email: TextView = findViewById(R.id.txtEmail)

        registrar.setOnClickListener{
            when{
                !email.text.isEmpty() && !validarEmail(email.text.toString()) ->{
                    Toast.makeText(baseContext, "Email no válido", Toast.LENGTH_SHORT).show()
                }
            }
            when {
               userName.text.isEmpty()  || contrasena.text.isEmpty() || email.text.isEmpty() || !validarEmail(email.text.toString()) ->{
                   Toast.makeText(baseContext, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()

                }else->{
                var intent: Intent = Intent(this, Bienvenida::class.java)
                intent.putExtra("username", userName.text.toString())
                startActivity(intent)
            }
            }
        }
    }

    fun validarEmail(email: String): Boolean {
        val regex = Regex("[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
        return regex.matches(email)
    }

}