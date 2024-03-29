package com.example.beclever.ui.home

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.beclever.ui.plus.LessonModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

/**
 * ViewModel per la gestione della schermata principale dell'applicazione.
 */
class HomeViewModel : ViewModel() {

    // LiveData per la lezione selezionata
    private val _lessonLiveData: MutableLiveData<LessonModel?> = MutableLiveData()
    val lesson: LiveData<LessonModel?>
        get() = _lessonLiveData

    private val db = FirebaseFirestore.getInstance()

    // LiveData per la lista delle lezioni filtrate
    private val _filteredLessonsList = MutableLiveData<List<LessonModel>>()
    val filteredLessonsList: LiveData<List<LessonModel>>
        get() = _filteredLessonsList

    /**
     * Verifica se esistono lezioni con i criteri di ricerca specificati.
     *
     * @param context Il contesto dell'applicazione.
     * @param subject Il soggetto della lezione.
     * @param date La data della lezione.
     * @param target Il target della lezione.
     * @param location La posizione della lezione.
     */
    fun checkIfLessonExists(context: Context, subject: String, date: String, target: String, location: String) {
        var query: Query = db.collection("lessons")

        if (subject.isNotEmpty()) {
            query = query.whereEqualTo("subject", subject)
        }

        if (date.isNotEmpty()) {
            query = query.whereEqualTo("date", date)
        }

        if (target.isNotEmpty()) {
            query = query.whereEqualTo("target", target)
        }

        if (location.isNotEmpty()) {
            query = query.whereEqualTo("location", location)
        }

        query.get()
            .addOnSuccessListener { querySnapshot ->
                val matchingLessons = mutableListOf<LessonModel>()

                for (lessonDocument in querySnapshot.documents) {
                    val lesson = lessonDocument.toObject(LessonModel::class.java)
                    lesson?.let { if (!it.isBooked) {
                        matchingLessons.add(it) // Aggiungi la lezione solo se non è prenotata
                    } }
                }

                if (matchingLessons.isEmpty()) {
                    // Nessuna lezione corrisponde ai criteri di ricerca
                    Toast.makeText(context, "Non ci sono lezioni corrispondenti", Toast.LENGTH_SHORT).show()
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

    /**
     * Cancella la lista delle lezioni filtrate.
     */
    fun clearFilteredLessonsList() {
        _filteredLessonsList.value = emptyList()
    }
}