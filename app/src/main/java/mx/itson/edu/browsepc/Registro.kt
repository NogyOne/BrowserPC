package mx.itson.edu.browsepc

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import org.w3c.dom.Text

class Registro : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("usuarios")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        val registrar: Button = findViewById(R.id.btnRegistrar)
        val userName: TextView = findViewById(R.id.txtUsername)
        val contrasena: TextView = findViewById(R.id.txtContrasena)
        val email: TextView = findViewById(R.id.txtEmail)
        val celular: TextView = findViewById(R.id.txtCelular)

        registrar.setOnClickListener{
            when{
                !email.text.isEmpty() && !validarEmail(email.text.toString()) ->{
                    Toast.makeText(baseContext, "Email no válido", Toast.LENGTH_SHORT).show()
                }
            }
            when {
               userName.text.isEmpty()  || contrasena.text.isEmpty() || email.text.isEmpty() || !validarEmail(email.text.toString()) ->{
                   Toast.makeText(baseContext, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()

                }else->{//Register
                val user = Usuario(userName.text.toString(), email.text.toString(), contrasena.text.toString(), celular.text.toString())
                collection.add(user).addOnSuccessListener { documentReference ->
                    Log.d(TAG, "Usuario agregado con ID: ${documentReference.id}")
                    var intent: Intent = Intent(this, Bienvenida::class.java)
                    intent.putExtra("username", userName.text.toString())
                    startActivity(intent)
                }
                    .addOnFailureListener{ e ->
                        Log.w(TAG, "Error al registrar usuario", e)
                    }
            }
            }
        }
    }

    fun validarEmail(email: String): Boolean {
        val regex = Regex("[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
        return regex.matches(email)
    }

}