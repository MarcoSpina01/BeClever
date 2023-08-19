package com.example.beclever.ui.profile

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore


class UserViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

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

    fun updateUserProfile(userId: String, newName: String, newSurname: String, newEmail: String, newBio: String, newQualification:String, context: Context?) {
        val userRef = db.collection("users").document(userId)
        userRef.update("first", newName, "last", newSurname, "email", newEmail, "bio", newBio, "qualification", newQualification )
            .addOnSuccessListener {
                Toast.makeText(context, "Profilo aggiornato con successo!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Errore durante la modifica", Toast.LENGTH_SHORT).show()
            }
    }
}