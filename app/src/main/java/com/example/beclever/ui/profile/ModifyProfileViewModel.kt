package com.example.beclever.ui.profile

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore

class ModifyProfileViewModel : ViewModel() {


    private val db = FirebaseFirestore.getInstance()

    fun getUserProfile(userId: String): MutableLiveData<User?> {
        val userProfileLiveData = MutableLiveData<User?>()

        db.collection("users").document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val userProfile = document.toObject(User::class.java)
                    userProfileLiveData.value = userProfile
                }
            }
            .addOnFailureListener { exception ->
                // Gestisci l'errore
            }

        return userProfileLiveData
    }

    fun updateUserProfile(userId: String, newName: String, newEmail: String, context: Context?) {
        val userRef = db.collection("users").document(userId)
        userRef.update("name", newName, "email", newEmail)
            .addOnSuccessListener {
                Toast.makeText(context, "Profilo aggiornato con successo!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(context, "Errore durante la modifica", Toast.LENGTH_SHORT).show()
            }
    }
}