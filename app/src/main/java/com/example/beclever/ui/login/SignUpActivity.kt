package com.example.beclever.ui.login

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.beclever.R
import com.example.beclever.databinding.ActivityRegisterBinding
import com.example.beclever.ui.profile.UserModel
import com.example.beclever.ui.profile.UserViewModel
import com.google.firebase.auth.FirebaseUser

/**
 * Activity per la registrazione di nuovi utenti.
 */
class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var userViewModel: UserViewModel

    /**
     * Callback per tornare all'attività di login.
     */
    fun backToLogin(view: TextView) {
        view.setOnClickListener {
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inizializza il ViewModel per gli utenti
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        // Imposta il click listener per il pulsante di registrazione
        binding.iscriviti.setOnClickListener {
            createUser()
        }

        // Imposta la funzione di torna al login
        val back = binding.BackToLogin
        backToLogin(back)
    }

    /**
     * Crea un nuovo utente utilizzando i dati inseriti dall'utente nel form di registrazione.
     */
    private fun createUser() {
        val email = binding.editTextTextEmailAddress.text.toString()
        val password = binding.editTextTextPassword.text.toString()
        val nome = binding.editTextTextPersonName.text.toString()
        val cognome = binding.editTextTextPersonName2.text.toString()

        // Crea un nuovo oggetto UserModel con i dati inseriti dall'utente
        val newUser = UserModel(nome, email, cognome, "", "", "", password)

        // Verifica se tutti i campi obbligatori sono stati inseriti
        if (!userViewModel.isRegistrationDataValid(newUser)) {
            Toast.makeText(this, "Inserisci tutti i campi obbligatori", Toast.LENGTH_SHORT).show()
            return
        }

        // Callback per gestire la registrazione
        val registrationCallback = object : RegistrationCallback {
            override fun onRegistrationSuccess(user: FirebaseUser?) {
                Toast.makeText(this@SignUpActivity, "Registrazione avvenuta con successo", Toast.LENGTH_SHORT).show()
                finish() // Chiudi l'attività di registrazione dopo il successo
            }

            override fun onRegistrationFailure(exception: Exception) {
                val errorMessage = "Errore: ${exception.message}"
                Toast.makeText(this@SignUpActivity, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }

        // Registra l'utente utilizzando il ViewModel
        userViewModel.registerUser(email, password, nome, cognome, registrationCallback)
    }
}

