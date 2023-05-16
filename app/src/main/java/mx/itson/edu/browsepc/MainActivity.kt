package mx.itson.edu.browsepc

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task

class MainActivity : AppCompatActivity() {
    lateinit var mGoogleSignInClient: GoogleSignInClient
    val RC_SIGN_IN = 343
    val LOG_OUT = 234
    private val collection = DbSingleton.getDb().collection("usuarios")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val login: Button = findViewById(R.id.btnLogin)
        val btnGmail: Button = findViewById(R.id.btnGmail)
        val crearCuenta: Button = findViewById(R.id.btnCrearCuenta)
        val olvideContrasena: Button = findViewById(R.id.btnForgotPassword)

        val userName: TextView = findViewById(R.id.txtUsername)
        val password: TextView = findViewById(R.id.txtContrasena)

        var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        btnGmail.setOnClickListener {
            val sigInIntent = mGoogleSignInClient.signInIntent
            startActivityForResult(sigInIntent, RC_SIGN_IN)
        }


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
    override fun onStart() {
        super.onStart()

        val account = GoogleSignIn.getLastSignedInAccount(this)
        updateUI(account)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == RC_SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }

        if(requestCode == LOG_OUT){
            signOut()
        }
    }

    private fun signOut(){
        mGoogleSignInClient.signOut().addOnCompleteListener(this, OnCompleteListener<Void?> {Toast.makeText(this,"sesion terminada", Toast.LENGTH_SHORT).show()} )
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>){
        try {
            val account = completedTask.getResult(ApiException::class.java)
            updateUI(account)
        } catch (e: ApiException){
            Log.w("test_signin", "signInResult:failed code=" + e.statusCode)
            updateUI(null)
        }
    }

    private fun updateUI(acct: GoogleSignInAccount?){
        if(acct != null){
            val intent = Intent(this, Bienvenida::class.java)
            //intent.putExtra("id", acct.getId())
            intent.putExtra("name", acct.getDisplayName())
            //intent.putExtra("email", acct.getEmail())
            startActivityForResult(intent, LOG_OUT)

        }

    }
}