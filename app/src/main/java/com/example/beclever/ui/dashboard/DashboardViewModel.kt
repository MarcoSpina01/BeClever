package com.example.beclever.ui.dashboard

import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.beclever.ui.plus.LessonModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class DashboardViewModel : ViewModel() {

    private val _bookedLessonsList = MutableLiveData<List<LessonModel>>()
    val bookedLessonsList: LiveData<List<LessonModel>> get() = _bookedLessonsList

    private val _publishedLessonsList = MutableLiveData<List<LessonModel>>()
    val publishedLessonsList: LiveData<List<LessonModel>> get() = _publishedLessonsList

    fun loadBookedLessons(userId: String) {
        // Carica le lezioni prenotate dall'utente da Firebase e aggiorna _bookedLessonsList
        // Esempio:
        Firebase.firestore.collection("lessons")
            .whereEqualTo("clientId", userId)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val lessons = querySnapshot.toObjects(LessonModel::class.java)
                _bookedLessonsList.postValue(lessons)
            }
            .addOnFailureListener {
            }
    }

    fun loadPublishedLessons(userId: String) {
        // Carica le lezioni pubblicate dall'utente da Firebase e aggiorna _publishedLessonsList
        // Esempio:
        Firebase.firestore.collection("lessons")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val lessons = querySnapshot.toObjects(LessonModel::class.java)
                _publishedLessonsList.postValue(lessons)
            }
            .addOnFailureListener {
            }
    }

    fun deleteLesson(lesson: LessonModel) {

        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid

        val db = Firebase.firestore
        val lessonsCollection = db.collection("lessons")


        // Trova il documento corrispondente alla lezione
        val lessonDocumentRef = lessonsCollection.document(lesson.lessonId)

        // Elimina il documento dalla collezione
        lessonDocumentRef.delete()
            .addOnSuccessListener {
                // Aggiorna la lista di lezioni pubblicate
                if (userId != null) {
                    loadPublishedLessons(userId)
                }
            }
            .addOnFailureListener { exception ->
                // Gestione dell'errore
            }
    }

     fun fetchUserInfoAndSetUsername(userId: String, usernameTextView: TextView) {
        val db = FirebaseFirestore.getInstance()
        val usersCollection = db.collection("users")

        usersCollection.document(userId).get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val userName = documentSnapshot.getString("first") ?: ""
                    usernameTextView.text = " ${userName}"
                }
            }
            .addOnFailureListener {
            }
    }

}