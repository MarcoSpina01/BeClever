package com.example.beclever.ui.profile

import android.content.Context
import android.widget.Toast
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

    fun updateUserProfile(userId: String, newName: String, newSurname: String, newEmail: String, newBio: String, newQualification: String, context: Context?) {userModel.updateUserProfile(userId, newName, newSurname, newEmail, newBio, newQualification)
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
            userModel.updatePassword(oldPassword, newPassword) -> onComplete(ChangePasswordResult.SUCCESS)
            !userModel.updatePassword(oldPassword, newPassword) -> onComplete(ChangePasswordResult.CURRENT_PASSWORD_INCORRECT)
            else -> onComplete(ChangePasswordResult.ERRORE)
        }
    }



}