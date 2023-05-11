package mx.itson.edu.browsepc

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.drawerlayout.widget.DrawerLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("usuarios")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val login: Button = findViewById(R.id.btnLogin)
        val  btnGmail: Button = findViewById(R.id.btnGmail)
        val crearCuenta: Button = findViewById(R.id.btnCrearCuenta)
        val olvideContrasena: Button = findViewById(R.id.btnForgotPassword)

        val userName: TextView = findViewById(R.id.txtUsername)
        val password: TextView = findViewById(R.id.txtContrasena)


        login.setOnClickListener{
            when{
                userName.text.isEmpty() || password.text.isEmpty()->{
                    Toast.makeText(baseContext, "Campos de usuario obligatorios", Toast.LENGTH_SHORT).show()
                }else->{
                    val queryUser = collection.whereEqualTo("username", userName.text.toString());
                    queryUser.get().addOnSuccessListener { document ->
                        if(document.size() == 1){
                            val userData = document.documents.get(0).data
                            val storePass = userData!!["password"] as String

                            if(password.text.toString() == storePass.toString()){
                                Log.d(ContentValues.TAG, "Usuario logueado con ID: ${document.documents.get(0).id}")
                                val user = Usuario(document.documents.get(0).id,userName.text.toString(), userData!!["email"] as String, userData!!["password"] as String, userData!!["celular"] as String)

                                UserSingleton.setUsuario(user)

                                var intent: Intent = Intent(this, Bienvenida::class.java)
                                intent.putExtra("username", userName.text.toString())
                                startActivity(intent)
                            }else{
                                Toast.makeText(baseContext, "Contraseña errónea", Toast.LENGTH_SHORT).show()
                            }
                        }else{
                            Toast.makeText(baseContext, "No se encontró ningún usuario", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
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