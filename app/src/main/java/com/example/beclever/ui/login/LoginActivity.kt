package com.example.beclever.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.beclever.ui.home.HomeActivity
import com.example.beclever.R
import com.example.beclever.databinding.ActivityLoginBinding

/**
 * Activity per la gestione dell'accesso degli utenti.
 */
class LoginActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel
    private val bindingView get() = _binding!!

    // Aggiungi una variabile di stato per gestire il login
    private var isLoggedIn = false

    /**
     * Funzione chiamata all'avvio dell'attività.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Imposta il layout dell'attività utilizzando Data Binding
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        bindingView.lifecycleOwner = this

        // Inizializza il ViewModel per l'accesso
        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        bindingView.viewModel = loginViewModel

        // Imposta un listener per il pulsante "Registrati"
        bindingView.registrati.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        // Imposta un listener per il pulsante di accesso
        bindingView.login.setOnClickListener {
            val username = bindingView.email.text.toString()
            val password = bindingView.password.text.toString()

            // Chiama la funzione di login nel ViewModel
            loginViewModel.loginUser(username, password)
        }

        // Chiude la tastiera virtuale quando si fa clic altrove nella schermata
        bindingView.root.setOnClickListener {
            bindingView.email.clearFocus()
            bindingView.password.clearFocus()
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(bindingView.root.windowToken, 0)
        }

        // Osserva la variabile loginSuccess nel ViewModel
        loginViewModel.loginSuccess.observe(this) { success ->
            if (success) {
                // Imposta la variabile di stato su "true" se il login ha successo
                isLoggedIn = true

                Toast.makeText(this, "Login effettuato", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                finish() // Chiudi l'activity dopo il login
            } else {
                Toast.makeText(this, "Login fallito", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Aggiungi un metodo per verificare lo stato di login nei tuoi test
    fun isUserLoggedIn(): Boolean {
        return isLoggedIn
    }
}




