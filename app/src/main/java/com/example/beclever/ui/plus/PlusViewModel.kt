package com.example.beclever.ui.plus

import android.content.Context
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase



class PlusViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is settings Fragment"
    }
    val text: LiveData<String> = _text

    private val db = Firebase.firestore


    //metodo per la creazione e storicizzazione di una nuova lezione
    fun createLesson(subject: String, date: String, target: String, location: String, cost: String, context: Context) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid

        val lesson = Lesson(subject, date, target, location, cost, userId)

        db.collection("lessons")
            .add(lesson)
            .addOnSuccessListener {
                Toast.makeText(context, "Lezione creata con successo!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Si è verificato un errore durante la creazione della lezione", Toast.LENGTH_SHORT).show()
            }
    }

}