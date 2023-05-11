package mx.itson.edu.browsepc

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object DbSingleton {

    private val db = FirebaseFirestore.getInstance()

    fun getDb(): FirebaseFirestore  = db

}