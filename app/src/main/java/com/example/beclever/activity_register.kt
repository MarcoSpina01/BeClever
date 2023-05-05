package com.example.beclever

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.beclever.ui.login.LoginActivity

class activity_register : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val back = findViewById<TextView>(R.id.BackToLogin)
        backToLogin(back)

    }

    fun backToLogin(view: TextView) {
        view.setOnClickListener {
            finish()
        }
    }

}