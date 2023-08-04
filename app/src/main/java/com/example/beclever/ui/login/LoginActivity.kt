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
}


