package com.example.beclever.ui.home

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.beclever.ui.plus.Lesson
import com.google.firebase.firestore.FirebaseFirestore

class HomeViewModel : ViewModel() {

    val _lessonLiveData: MutableLiveData<Lesson?> = MutableLiveData()
    val lesson: LiveData<Lesson?>
        get() = _lessonLiveData

    val db = FirebaseFirestore.getInstance()

    private val _filteredLessonsList = MutableLiveData<List<Lesson>>()
    val filteredLessonsList: LiveData<List<Lesson>>
        get() = _filteredLessonsList

    fun checkIfLessonExists(context: Context, subject: String, date: String, target: String, location: String) {


        db.collection("lessons")
            .whereEqualTo("subject", subject)
            .whereEqualTo("date", date)
            .whereEqualTo("target", target)
            .whereEqualTo("location", location)
            .get()
                .addOnSuccessListener { querySnapshot ->
                    val matchingLessons = mutableListOf<Lesson>()
                    for (lessonDocument in querySnapshot) {
                        val lesson = lessonDocument.toObject(Lesson::class.java)
                        matchingLessons.add(lesson)
                    }

                    if (querySnapshot.isEmpty) {
                        // Nessuna lezione corrisponde ai criteri di ricerca
                        //Toast.makeText(context, "Lezione non trovata", Toast.LENGTH_SHORT).show()
                        _filteredLessonsList.postValue(emptyList())

                    } else {
                        // Almeno una lezione corrisponde ai criteri di ricerca
                        _filteredLessonsList.postValue(matchingLessons)
                    }
                }
            .addOnFailureListener {
                // Gestione dell'errore nel caso la query fallisca
                Toast.makeText(context, "Errore durante la ricerca della lezione", Toast.LENGTH_SHORT).show()
            }
    }
}