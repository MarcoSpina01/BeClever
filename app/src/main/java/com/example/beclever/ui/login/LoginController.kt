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

    fun login(username: String, password: String, context : Context) {

        userModel.username = username
        userModel.password = password
        auth = FirebaseAuth.getInstance()

        if (userModel.username.toString().isEmpty() || userModel.password.toString().isEmpty())
            Toast.makeText(
                context,
                "Inserisci l'email e la password.",
                Toast.LENGTH_SHORT
            ).show()
        else {
            // L'utente ha inserito l'email e la password, quindi procedi con il login.
            auth.signInWithEmailAndPassword(
                userModel.username.toString(),
                userModel.password.toString()
            )
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Toast.makeText(
                            context,
                            "Autenticazione riuscita.",
                            Toast.LENGTH_SHORT,
                        ).show()
                        val user = auth.currentUser
                        val intent = Intent(context, HomeActivity::class.java)
                        startActivity(context, intent, null)


                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(
                            context,
                            "Authentication failed.",
                            Toast.LENGTH_SHORT,
                        ).show()
                        //updateUI(null)
                    }
                }
        }
    }

        // Gestione della logica di business per il login...
    fun isLoggedIn(): Boolean {
            auth = FirebaseAuth.getInstance()
            return auth.currentUser != null
    }
}
