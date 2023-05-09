package mx.itson.edu.browsepc

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore

class NuevaContrasena : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()
    private val collectionUsers = db.collection("usuarios")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nueva_contrasena)

        var continuar: Button = findViewById(R.id.btnConfirmar)
        val bundle = intent.extras

        val txtContrasena: TextView = findViewById(R.id.txtContrasenaNew)
        val txtConfirmacion: TextView = findViewById(R.id.txtConfirmarCont)

        continuar.setOnClickListener{
            if(bundle != null){
                if(txtContrasena.text.toString() == txtConfirmacion.text.toString()){
                    val query = collectionUsers.whereEqualTo("email", bundle.getString("email"))
                    query.get().addOnSuccessListener { documents ->
                        for (document in documents){
                            val docRef = collectionUsers.document(document.id)
                            docRef.update("password", txtContrasena.text.toString())
                                .addOnSuccessListener {
                                    Log.d(ContentValues.TAG, "Contraseña restablecida de usuario: ${document.id}")
                                    Toast.makeText(baseContext, "Reestablecimiento de contraseña exitoso", Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener{ e ->
                                    Log.w(ContentValues.TAG, "Error al reestableces contraseña", e)
                                    Toast.makeText(baseContext, "Error al reestablecer contraseña, intenta de nuevo", Toast.LENGTH_SHORT).show()
                                }
                        }
                        var intent: Intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    }
                }else{
                    Toast.makeText(baseContext, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}