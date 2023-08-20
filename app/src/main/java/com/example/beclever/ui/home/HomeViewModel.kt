package com.example.beclever.ui.home

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.beclever.ui.plus.Lesson
import com.example.beclever.ui.profile.User
import com.example.beclever.ui.profile.UserModel
import com.google.firebase.firestore.FirebaseFirestore

class HomeViewModel : ViewModel() {

    val _lessonLiveData: MutableLiveData<Lesson?> = MutableLiveData()
    val lesson: LiveData<Lesson?>
        get() = _lessonLiveData

    fun checkIfLessonExists(context: Context, subject: String, date: String, target: String, location: String, cost: String) {

        val db = FirebaseFirestore.getInstance()

        val query = when {
            cost.contains("0 - 10") -> db.collection("lessons").whereLessThanOrEqualTo("cost".replace("€","").trim(), 10)
            cost.contains("10 - 20") -> db.collection("lessons").whereGreaterThanOrEqualTo("cost".replace("€","").trim(), 10).whereLessThanOrEqualTo("cost", 20)
            cost.contains("20 +") -> db.collection("lessons").whereGreaterThanOrEqualTo("cost".replace("€","").trim(), 20)
            cost.contains("Qualsiasi") -> db.collection("lessons")
            else -> return // Handle the case where the cost format is not recognized
        }

        query.whereEqualTo("subject", subject)
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

                    if (matchingLessons.isNotEmpty()) {
                        // Almeno una lezione corrisponde ai criteri di ricerca
                        _lessonLiveData.value = matchingLessons[0]
                        Toast.makeText(context, "Lezione trovata", Toast.LENGTH_SHORT).show()
                    } else {
                        // Nessuna lezione corrisponde ai criteri di ricerca
                        _lessonLiveData.value = null
                        Toast.makeText(context, "Lezione non trovata", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { exception ->
                    // Gestione dell'errore nel caso la query fallisca
                    _lessonLiveData.value = null
                    Toast.makeText(context, "Errore durante la ricerca della lezione", Toast.LENGTH_SHORT).show()
                }
    }
}