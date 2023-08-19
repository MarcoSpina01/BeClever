package com.example.beclever.ui.login

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beclever.ui.profile.User
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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

