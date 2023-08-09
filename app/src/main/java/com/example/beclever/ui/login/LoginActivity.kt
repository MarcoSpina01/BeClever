package com.example.beclever.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.beclever.HomeActivity
import com.example.beclever.databinding.ActivityLoginBinding
import com.example.beclever.activity_register

class LoginActivity : AppCompatActivity() {
/*
    private lateinit var binding: ActivityLoginBinding
    private val loginController = LoginController()


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        autoLogin()

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.registrati.setOnClickListener {
            val intent = Intent(this, activity_register::class.java)
            startActivity(intent)
        }

        binding.login.setOnClickListener {
            loginUser()
        }
        binding.root.setOnTouchListener { _, _ ->
            // Pulisci il focus dai campi di input
            binding.email.clearFocus()
            binding.password.clearFocus()
            // Nascondi la tastiera
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(binding.root.windowToken, 0)
            false
        }
    }

    private fun loginUser() {
        val username = binding.email.text.toString()
        val password = binding.password.text.toString()
        val callback: (Boolean) -> Unit = { success ->
            if (success) {
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        loginController.login(username, password, this, callback)
    }

    private fun autoLogin() {
        if (loginController.isLoggedIn()) {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

 */

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater) // Inizializzo il binding utilizzando il layout dell'ActivityLoginBinding
        setContentView(binding.root)

        val loginController = LoginController() // Inizializzo il LoginController e lo passo al LoginViewModel
        viewModel = LoginViewModel(loginController)

        binding.registrati.setOnClickListener { // Imposto un listener per il pulsante "Registrati"
            val intent = Intent(this, activity_register::class.java)
            startActivity(intent)
        }

        binding.login.setOnClickListener { // Imposto un listener per il pulsante "Login"
            loginUser()
        }

        binding.root.setOnTouchListener { _, _ -> // Imposto un listener per gestire il tocco del layout

            binding.email.clearFocus() // Pulisce il focus dal campo email
            binding.password.clearFocus() // Pulisce il focus dal campo password
            // Nasconde la tastiera
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(binding.root.windowToken, 0)
            false
        }
    }

    private fun loginUser() {
        val username = binding.email.text.toString()
        val password = binding.password.text.toString()

        viewModel.loginUser(username, password, this) // Chiamata al metodo loginUser del ViewModel passando username e password

        viewModel.loginSuccess.observe(this, { success -> // observer che osserva il LiveData loginSuccess per ottenere il risultato dell'autenticazione
            if (success) { // Se l'autenticazione è riuscita, avvia HomeActivity
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
            } else { // Se l'autenticazione è fallita, mostra un messaggio di errore
                Toast.makeText(this, "Autenticazione fallita.", Toast.LENGTH_SHORT).show()
            }
        })
    }

}



