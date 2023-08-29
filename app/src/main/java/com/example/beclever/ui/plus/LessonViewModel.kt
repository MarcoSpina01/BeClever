package com.example.beclever.ui.plus

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class LessonViewModel : ViewModel() {

    //private lateinit var auth: FirebaseAuth

    private val db = Firebase.firestore

    fun createLesson(subject: String, date: String, target: String, location: String, cost: String, callback: (Boolean) -> Unit) {

        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid

        val lessonModel = LessonModel(subject, date, target, location, cost, userId, false, "", "", null, null, false, null, null)

        db.collection("lessons")
            .add(lessonModel)
            .addOnSuccessListener { documentReference ->
                val lessonId = documentReference.id // Ottieni il nome del documento generato da Firebase
                // Ora aggiorna il campo lessonId nel documento con il nome del documento stesso
                db.collection("lessons")
                    .document(lessonId)
                    .update("lessonId", lessonId)
                    .addOnSuccessListener {
                        callback(true)
                    }
                    .addOnFailureListener {
                        callback(false)
                    }
            }
            .addOnFailureListener {
                callback(false)
            }


    }
}
