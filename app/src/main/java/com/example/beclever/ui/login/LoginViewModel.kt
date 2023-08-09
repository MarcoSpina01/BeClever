package com.example.beclever.ui.login

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel(private val loginController: LoginController) : ViewModel() {

    private val _loginSuccess = MutableLiveData<Boolean>()
    val loginSuccess: LiveData<Boolean>
        get() = _loginSuccess

    fun loginUser(username: String, password: String, context: Context) {
        // Chiama il metodo login del LoginController e passa il context e il callback
        loginController.login(username, password, context) { success ->
            // Aggiorna il MutableLiveData con il valore di success
            _loginSuccess.value = success
        }
    }
}

