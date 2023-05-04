package com.example.beclever.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.beclever.R
import com.example.beclever.databinding.ActivityLoginBinding
import com.example.beclever.activity_register
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {


    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        FirebaseApp.initializeApp(this)

        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val database = FirebaseDatabase.getInstance("https://beclever-60197-default-rtdb.europe-west1.firebasedatabase.app/")
        val myRef = database.reference
        val auth = FirebaseAuth.getInstance()

        val email = findViewById<EditText>(R.id.email)
        val password = findViewById<EditText>(R.id.password)

        auth.signInWithEmailAndPassword(email.text.toString(), password.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Login avvenuto con successo", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Login fallito", Toast.LENGTH_SHORT).show()
                }
            }

        binding.registrati?.setOnClickListener {
            val intent = Intent(this, activity_register::class.java)
            startActivity(intent)
            finish()
        }
    }

}