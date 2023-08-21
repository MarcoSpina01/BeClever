package com.example.beclever

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.example.beclever.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class activity_register : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    //private lateinit var database: FirebaseDatabase

// Nella funzione onCreate() dell'attività
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.iscriviti.setOnClickListener {
        createUser()
        }

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

    private fun registerUser(email: String, password: String, nome : String, cognome : String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(baseContext, "registrazione avvenuta", Toast.LENGTH_SHORT).show()
                    val db = Firebase.firestore
                    val user = hashMapOf(
                        "first" to nome,
                        "last" to cognome,
                        "email" to email,
                        "bio" to "",
                        "qualification" to "",

                    )
                    var utente = auth.currentUser
                    var id = utente?.uid
                    if (id != null) {
                        db.collection("users")
                            .document(id)
                            .set(user)
                            .addOnSuccessListener {
                                // Il documento è stato creato con successo
                                println("Documento creato con successo")
                            }
                            .addOnFailureListener { e ->
                                // Si è verificato un errore durante la creazione del documento
                                println("Errore durante la creazione del documento: $e")
                            }
                        finish()
                    }
                    // Eseguire qui l'accesso all'account
                } else {
                    // Si è verificato un errore durante la registrazione
                    Toast.makeText(baseContext, "Errore durante la registrazione: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun createUser() {

            val email = binding.editTextTextEmailAddress.text.toString()
            val password = binding.editTextTextPassword.text.toString()
            val nome = binding.editTextTextPersonName.text.toString()
            val cognome = binding.editTextTextPersonName2.text.toString()
            registerUser(email, password, nome, cognome)

    }

}