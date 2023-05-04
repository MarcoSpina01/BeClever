package com.example.beclever.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.beclever.databinding.ActivityLoginBinding
import com.example.beclever.activity_register

class LoginActivity : AppCompatActivity() {


    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.registrati?.setOnClickListener {
            val intent = Intent(this, activity_register::class.java)
            startActivity(intent)
            finish()
        }
    }

}