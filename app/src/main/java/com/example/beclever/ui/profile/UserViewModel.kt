package com.example.beclever.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class UserViewModel : ViewModel() {

    private val userModel = UserModel()

    private val _user = MutableLiveData<User>()

    val user: LiveData<User>
        get() = _user

    fun fetchUserData() {
        userModel.fetchUserData { user ->
            if (user != null) {
                _user.value = user
            } else {
                // Nessun dato corrispondente trovato
            }
        }
    }
}