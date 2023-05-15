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
    private val collectionUsers = DbSingleton.getDb().collection("usuarios")
    private val collectionFavs = DbSingleton.getDb().collection("favoritos")
    private val collectionCart = DbSingleton.getDb().collection("carrito")
    private val collectionCartProd = DbSingleton.getDb().collection("carrito_productos")

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

                val query = collectionUsers.whereEqualTo("email", email.text.toString())
                query.get().addOnSuccessListener { document ->
                    if(document.size() == 0){
                        if(registro(user)){
                            var intent: Intent = Intent(this, Bienvenida::class.java)
                            intent.putExtra("username", user.username)
                            startActivity(intent)
                        }else{
                            Toast.makeText(baseContext, "Ha ocurrido un error con el registro, vuelve a intentar", Toast.LENGTH_SHORT).show()
                        }
                    }else if(document.size() >= 1){
                        Toast.makeText(baseContext, "El correo ya está asociado a otro usuario", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            }
        }
    }

    fun registro(user: Usuario): Boolean {
        var result: Boolean = true
        collectionUsers.add(user).addOnSuccessListener { userReference ->
            UserSingleton.setUsuario(user)
            Log.d(TAG, "Usuario agregado con ID: ${userReference.id}")

            val carrito = CarritoDom(userReference.id)
            val favoritos = FavoritosDom(userReference.id)

            collectionCart.add(carrito).addOnSuccessListener { cartReference ->
                Log.d(TAG, "Carrito creado y agregado con ID: ${cartReference.id}")

                /*val carritoProds = Carrito_Productos(cartReference.id)
                collectionCartProd.add(carritoProds).addOnSuccessListener { cartProdsReference ->
                    Log.d(TAG, "CarritoProductos creado y agregado con ID: ${cartProdsReference.id} ${carritoProds.id_carrito}")
                }
                    .addOnFailureListener{ e ->
                        Log.w(TAG, "Error al registrar CarritoProductos", e)
                        result = false
                    }*/
            }
                .addOnFailureListener{ e ->
                    Log.w(TAG, "Error al registrar carrito", e)
                    result = false
                }

            collectionFavs.add(favoritos).addOnSuccessListener { favsReference ->
                Log.d(TAG, "Favoritos creado y agregado con ID: ${favsReference.id}")
            }
                .addOnFailureListener{ e ->
                    Log.w(TAG, "Error al registrar favoritos", e)
                    result = false
                }
        }
            .addOnFailureListener{ e ->
                Log.w(TAG, "Error al registrar usuario", e)
                result = false
            }

        return result
    }

    fun validarEmail(email: String): Boolean {
        val regex = Regex("[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
        return regex.matches(email)
    }

}