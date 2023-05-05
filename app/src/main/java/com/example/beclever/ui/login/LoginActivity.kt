package com.example.beclever.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.beclever.databinding.ActivityLoginBinding
import com.example.beclever.activity_register
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {


    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.registrati?.setOnClickListener {
            val intent = Intent(this, activity_register::class.java)
            startActivity(intent)

        }
    }

    fun setCredential(view: View) {
        val database = Firebase.database
        val myRef = database.getReference("message")
        myRef.setValue("Hello, World!")
    }


}