package com.example.beclever

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.beclever.databinding.ActivityLoginBinding
import com.example.beclever.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.example.beclever.ui.login.LoginActivity
import com.google.firebase.database.FirebaseDatabase

class activity_register : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    //private lateinit var database: FirebaseDatabase

// Nella funzione onCreate() dell'attività
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


        //database = FirebaseDatabase.getInstance()


        auth = FirebaseAuth.getInstance()

        val back = findViewById<TextView>(R.id.BackToLogin)
        backToLogin(back)

        //val newUser = database.reference

    }

    fun backToLogin(view: TextView) {
        view.setOnClickListener {
            finish()
        }
    }

    private fun registerUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(baseContext, "registrazione avvenuta: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    val user = auth.currentUser
                    // Eseguire qui l'accesso all'account
                } else {
                    // Si è verificato un errore durante la registrazione
                    Toast.makeText(baseContext, "Errore durante la registrazione: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    fun createUser(view: TextView){
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.iscriviti.setOnClickListener{
            val email = binding.editTextTextEmailAddress.text.toString()
            val password = binding.editTextTextPassword.text.toString()
            registerUser(email, password)
        }
    }
}