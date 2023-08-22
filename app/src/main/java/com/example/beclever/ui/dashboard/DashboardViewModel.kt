package com.example.beclever.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.beclever.ui.plus.LessonModel
import com.google.firebase.auth.FirebaseAuth
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
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val lessons = querySnapshot.toObjects(LessonModel::class.java)
                _bookedLessonsList.postValue(lessons)
            }
            .addOnFailureListener { exception ->
                // Gestione dell'errore
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
            .addOnFailureListener { exception ->
                // Gestione dell'errore
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

}