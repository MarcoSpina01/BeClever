package com.example.beclever.ui.login

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.beclever.databinding.ActivityLoginBinding
import com.example.beclever.activity_register
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {


    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth



    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.registrati?.setOnClickListener {
            val intent = Intent(this, activity_register::class.java)
            startActivity(intent)



        }
    }

    fun loginUser(view: View) {
        auth = FirebaseAuth.getInstance()
        binding.login.setOnClickListener {
            val database = Firebase.database
            val email = binding.email?.text.toString()
            val password = binding.password?.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                // Mostra un messaggio all'utente per informarlo di inserire l'email e la password.
                Toast.makeText(
                    baseContext,
                    "Inserisci l'email e la password.",
                    Toast.LENGTH_SHORT,
                ).show()
            } else {
                // L'utente ha inserito l'email e la password, quindi procedi con il login.
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success")
                            Toast.makeText(
                                baseContext,
                                "Autenticazione riuscita.",
                                Toast.LENGTH_SHORT,
                            ).show()
                            val user = auth.currentUser
                            //updateUI(user)
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.exception)
                            Toast.makeText(
                                baseContext,
                                "Authentication failed.",
                                Toast.LENGTH_SHORT,
                            ).show()
                            //updateUI(null)
                        }
                    }
            }
        }
    }
}
