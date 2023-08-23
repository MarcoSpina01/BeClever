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

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var userViewModel: UserViewModel



// Nella funzione onCreate() dell'attività
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.iscriviti.setOnClickListener {
            createUser()
        }

        val back = binding.BackToLogin
        backToLogin(back)

    }

    fun backToLogin(view: TextView) {
        view.setOnClickListener {
            finish()
        }
    }



    private fun createUser() {

        val email = binding.editTextTextEmailAddress.text.toString()
        val password = binding.editTextTextPassword.text.toString()
        val nome = binding.editTextTextPersonName.text.toString()
        val cognome = binding.editTextTextPersonName2.text.toString()
        val newUser = UserModel(nome, email, cognome, "", "", "", password)
        if (!userViewModel.isRegistrationDataValid(newUser)) {
            Toast.makeText(this, "Inserisci tutti i campi obbligatori", Toast.LENGTH_SHORT).show()
            return
        }
        val registrationCallback = object : RegistrationCallback {
            override fun onRegistrationSuccess(user: FirebaseUser?) {
                Toast.makeText(this@SignUpActivity, "Registrazione avvenuta con successo", Toast.LENGTH_SHORT).show()
                finish()
            }

            override fun onRegistrationFailure(exception: Exception) {
                val errorMessage = "Error: ${exception.message}"
                Toast.makeText(this@SignUpActivity, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }

        userViewModel.registerUser(email, password, nome, cognome, registrationCallback)
    }

}

