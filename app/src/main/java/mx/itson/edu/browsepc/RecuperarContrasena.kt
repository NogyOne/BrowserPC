package mx.itson.edu.browsepc

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import org.w3c.dom.Text

class RecuperarContrasena : AppCompatActivity() {
    private val collection = DbSingleton.getDb().collection("usuarios")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recuperar_contrasena)

        var continuar: Button = findViewById(R.id.btnContinuar)

        val txtEmail: TextView = findViewById(R.id.txtCorreo)
        val txtCelular: TextView = findViewById(R.id.txtNumero)

        continuar.setOnClickListener{
            when {
                txtEmail.text.isEmpty() || txtCelular.text.isEmpty() -> {
                    Toast.makeText(baseContext, "Campos obligatorios para la recuperación", Toast.LENGTH_SHORT).show()
                }else -> {
                    val query = collection.whereEqualTo("email", txtEmail.text.toString())
                    query.get().addOnSuccessListener { document ->
                        if(document.size() == 1){
                            val userData = document.documents.get(0).data
                            val storeNum = userData!!["celular"] as String

                            if(txtCelular.text.toString() == storeNum){
                                var intent: Intent = Intent(this, NuevaContrasena::class.java)
                                intent.putExtra("email", txtEmail.text.toString())
                                startActivity(intent)
                            }else{
                                Toast.makeText(baseContext, "El email y número telefónico no coinciden", Toast.LENGTH_SHORT).show()
                            }
                        }else{
                            Toast.makeText(baseContext, "No se encontró ningún usuario vinculado al correo ingresado", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

        }
    }
}