package com.example.beclever.ui.dashboard

import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.beclever.ui.notifications.NotificationsViewModel
import com.example.beclever.ui.plus.LessonModel
import com.example.beclever.ui.profile.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


/**
 * ViewModel per la dashboard dell'utente.
 */
class DashboardViewModel : ViewModel() {

    private val _bookedLessonsList = MutableLiveData<List<LessonModel>>()
    val bookedLessonsList: LiveData<List<LessonModel>> get() = _bookedLessonsList

    private val _publishedLessonsList = MutableLiveData<List<LessonModel>>()
    val publishedLessonsList: LiveData<List<LessonModel>> get() = _publishedLessonsList

    /**
     * Carica le lezioni prenotate dall'utente da Firebase e aggiorna [_bookedLessonsList].
     *
     * @param userId ID dell'utente corrente.
     */
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
                // Gestione dell'errore
            }
    }

    /**
     * Carica le lezioni pubblicate dall'utente da Firebase e aggiorna [_publishedLessonsList].
     *
     * @param userId ID dell'utente corrente.
     */
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
                // Gestione dell'errore
            }
    }

    /**
     * Elimina una lezione dal database Firebase.
     *
     * @param lesson Lezione da eliminare.
     */
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
            .addOnFailureListener {
                // Gestione dell'errore
            }
    }

    /**
     * Ottiene le informazioni dell'utente e imposta il nome utente in una TextView.
     *
     * @param userId ID dell'utente di cui ottenere le informazioni.
     * @param usernameTextView TextView in cui impostare il nome utente.
     */
    fun fetchUserInfoAndSetUsername(userId: String, usernameTextView: TextView) {
        val db = FirebaseFirestore.getInstance()
        val usersCollection = db.collection("users")

        usersCollection.document(userId).get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val userName = documentSnapshot.getString("first") ?: ""
                    usernameTextView.text = userName
                }
            }
            .addOnFailureListener {
                // Gestione dell'errore
            }
    }

    /**
     * Cancella una prenotazione di lezione nel database Firebase.
     *
     * @param lesson Lezione da cui cancellare la prenotazione.
     */
    fun deleteBooking(lesson: LessonModel) {
        val lessonId = lesson.lessonId
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid

        val db = FirebaseFirestore.getInstance()
        val lessonsCollection = db.collection("lessons")

        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.BASIC_ISO_DATE
        val currentDate = current.format(formatter).toLong()

        val formatter2 = DateTimeFormatter.ISO_LOCAL_TIME
        val currentTime = current.format(formatter2).toString().replace(":", "").substringBefore(".").toLong()

        // Crea una query complessa che corrisponde a tutti i campi specificati
        val query = lessonsCollection
            .whereEqualTo("lessonId", lessonId)

        query.get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot.documents) {
                    val lessonDocumentRef = lessonsCollection.document(document.id)
                    lessonDocumentRef.update(
                        "booked", false,
                        "clientId", "",
                        "dateBooking", null,
                        "timeBooking", null,
                        "canceled", true,
                        "dateCancel", currentDate,
                        "timeCancel", currentTime
                    )
                        .addOnSuccessListener {
                            // Aggiorna la lista di lezioni prenotate
                            if (userId != null) {
                                loadBookedLessons(userId)
                            }
                        }
                }
            }
    }
}