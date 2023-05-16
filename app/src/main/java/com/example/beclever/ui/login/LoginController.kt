package com.example.beclever.ui.login


import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.example.beclever.HomeActivity
import com.google.firebase.auth.FirebaseAuth

class LoginController {

    private val userModel = UserModel()
    private lateinit var auth: FirebaseAuth

    fun login(username: String, password: String, context: Context, callback: (Boolean) -> Unit) {
        userModel.username = username
        userModel.password = password
        auth = FirebaseAuth.getInstance()

        if (userModel.username.toString().isEmpty() || userModel.password.toString().isEmpty()) {
            Toast.makeText(context, "Inserisci l'email e la password.", Toast.LENGTH_SHORT).show()
            callback(false)
        } else {
            auth.signInWithEmailAndPassword(
                userModel.username.toString(),
                userModel.password.toString()
            ).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context, "Autenticazione riuscita.", Toast.LENGTH_SHORT).show()
                    callback(true)
                } else {
                    Toast.makeText(context, "Autenticazione fallita.", Toast.LENGTH_SHORT).show()
                    callback(false)
                }
            }
        }
    }

        // Gestione della logica di business per il login...
    fun isLoggedIn(): Boolean {
            return auth.currentUser != null
    }
}
