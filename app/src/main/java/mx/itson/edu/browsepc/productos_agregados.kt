package mx.itson.edu.browsepc

import android.content.ContentValues.TAG
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class productos_agregados : AppCompatActivity() {
    val db = Firebase.firestore
    val collectionFavoritos = db.collection("favoritos").document(UserSingleton.getUsuario().id)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_productos_agregados)

        val btnDelete: Button = findViewById(R.id.btnDelete)
        val tvNombre: TextView = findViewById(R.id.tv_nombre)


        btnDelete.setOnClickListener{
            println("Cuando por las noches")
            collectionFavoritos.update("productos", FieldValue.arrayRemove(tvNombre.text.toString()))
                .addOnSuccessListener {
                    Log.d(TAG, "Producto eliminado: ${tvNombre.text.toString()}")
                    Toast.makeText(baseContext, "Producto eliminado de productos", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener{
                    Log.w(TAG, "Error al eliminar producto de favoritos")
                    Toast.makeText(baseContext, "Error al eliminar producto, intenta de nuevo", Toast.LENGTH_SHORT).show()
                }
        }
    }
}