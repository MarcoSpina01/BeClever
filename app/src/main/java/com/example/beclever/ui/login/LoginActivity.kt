package com.example.beclever.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.beclever.HomeActivity
import com.example.beclever.R
import com.example.beclever.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {


    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel
    private val bindingView get() = binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.lifecycleOwner = this

        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        binding.viewModel = loginViewModel

        binding.registrati.setOnClickListener { // Imposto un listener per il pulsante "Registrati"
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        binding.login.setOnClickListener {
            val username = binding.email.text.toString()
            val password = binding.password.text.toString()

            // Chiama la funzione di login nel ViewModel
            loginViewModel.loginUser(username, password)
        }

        binding.root.setOnClickListener() {
            binding.email.clearFocus()
            binding.password.clearFocus()
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(binding.root.windowToken, 0)
            false
        }

        // Osserva loginSuccess nel ViewModel
        loginViewModel.loginSuccess.observe(this) { success ->
            if (success) {
                Toast.makeText(this, "Login effettuato", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                finish()
            } else {
                Toast.makeText(this, "Login fallito", Toast.LENGTH_SHORT).show()
            }
        }
    }
}




