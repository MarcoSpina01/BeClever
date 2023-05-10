package com.example.beclever.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.beclever.HomeActivity
import com.example.beclever.databinding.ActivityLoginBinding
import com.example.beclever.activity_register
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {


    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private val loginController = LoginController()


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        autoLogin()

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.registrati?.setOnClickListener {
            val intent = Intent(this, activity_register::class.java)
            startActivity(intent)

            binding.login?.setOnClickListener {

            }
        }
    }

    fun loginUser(view: View) {
        val username = binding.email.text.toString()
        val password = binding.password.text.toString()
        val context = this
        loginController.login(username, password, context)
        if (loginController.isLoggedIn()) {
            finish()
        }

    }

    private fun autoLogin() {
        if (loginController.isLoggedIn()) {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}


