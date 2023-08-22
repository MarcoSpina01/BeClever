package com.example.beclever.ui.profile

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class UserViewModel : ViewModel() {


    private val userRepository = UserRepository()

    private val _userModel = MutableLiveData<UserModel>()
    val userModel: LiveData<UserModel>
        get() = _userModel


    fun fetchUserData() {
        userRepository.fetchUserData { user ->
            if (user != null) {
                _userModel.value = user
            } else {
                // Nessun dato corrispondente trovato
            }
        }
    }

    fun updateUserProfile(userId: String, newName: String, newSurname: String, newEmail: String, newBio: String, newQualification: String, context: Context?) {userRepository.updateUserProfile(userId, newName, newSurname, newEmail, newBio, newQualification)
        .addOnSuccessListener {
            Toast.makeText(context, "Profilo aggiornato con successo!", Toast.LENGTH_SHORT).show()
            fetchUserData()
        }
        .addOnFailureListener {
            Toast.makeText(context, "Errore durante la modifica", Toast.LENGTH_SHORT).show()
        }
    }

    fun updatePassword(
        oldPassword: String,
        newPassword: String,
        onComplete: (ChangePasswordResult) -> Unit
    ) {
        when {
            newPassword == oldPassword -> onComplete(ChangePasswordResult.NEW_PASSWORD_DIFFERENT)
            newPassword.length < 8 -> onComplete(ChangePasswordResult.NEW_PASSWORD_TOO_SHORT)
            userRepository.updatePassword(oldPassword, newPassword) -> onComplete(ChangePasswordResult.SUCCESS)
            !userRepository.updatePassword(oldPassword, newPassword) -> onComplete(ChangePasswordResult.CURRENT_PASSWORD_INCORRECT)
            else -> onComplete(ChangePasswordResult.ERRORE)
        }
    }



}