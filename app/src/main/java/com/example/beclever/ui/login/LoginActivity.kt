package com.example.beclever.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.beclever.HomeActivity
import com.example.beclever.databinding.ActivityLoginBinding
import com.example.beclever.activity_register
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {


    private lateinit var binding: ActivityLoginBinding
    private val loginController = LoginController()


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        FirebaseAuth.getInstance().signOut()
//        autoLogin()


        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.registrati.setOnClickListener {
            val intent = Intent(this, activity_register::class.java)
            startActivity(intent)
        }

        binding.login.setOnClickListener {
            loginUser()
        }
    }

    private fun loginUser() {
        val username = binding.email.text.toString()
        val password = binding.password.text.toString()
        val context = this
        loginController.login(username, password, context)
        if (loginController.isLoggedIn()) {
            val intent = Intent(context, HomeActivity::class.java)
            ContextCompat.startActivity(context, intent, null)
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


