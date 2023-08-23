package com.example.beclever.ui.login

import com.google.firebase.auth.FirebaseUser

interface RegistrationCallback {
    fun onRegistrationSuccess(user: FirebaseUser?)
    fun onRegistrationFailure(exception: Exception)
}