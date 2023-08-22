package com.example.beclever.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class LoginViewModel() : ViewModel() {

    private val _loginSuccess = MutableLiveData<Boolean>()
    val loginSuccess: LiveData<Boolean>
        get() = _loginSuccess

    fun loginUser(username: String, password: String) {
        val auth = FirebaseAuth.getInstance()

        if (username.isEmpty() || password.isEmpty()) {
            _loginSuccess.value = false
        } else {
            auth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener { task ->
                    _loginSuccess.value = task.isSuccessful
                }
        }
    }


}

