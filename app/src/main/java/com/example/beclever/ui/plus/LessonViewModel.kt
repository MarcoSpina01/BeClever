package com.example.beclever.ui.plus

import android.content.Context
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class LessonViewModel : ViewModel() {

    //private lateinit var auth: FirebaseAuth

    private val db = Firebase.firestore

    fun createLesson(subject: String, date: String, target: String, location: String, cost: String, hour: String, callback: (Boolean) -> Unit) {

        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid
        if(!isDateAfterOrToday(date)) {
            callback(false)
            return
        }
        if(!isCostANumber(cost)) {
            callback(false)
            return
        }
        val lessonModel = LessonModel(subject, date, target, location, cost, hour, userId, false, "", "", null, null, false, null, null)

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
            }
    }

    fun updateLesson(userId: String,subject: String, target: String, location: String, date: String, cost: String, clientId: String, context: Context) {
        val db = FirebaseFirestore.getInstance()
        val lessonsCollection = db.collection("lessons")

        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.BASIC_ISO_DATE
        val currentDate = current.format(formatter).toLong()

        val formatter2 = DateTimeFormatter.ISO_LOCAL_TIME
        val currentTime = current.format(formatter2).toString().replace(":", "").substringBefore(".").toLong()

        // Crea una query complessa che corrisponde a tutti i campi specificati
        val query = lessonsCollection
            .whereEqualTo("userId", userId)
            .whereEqualTo("subject", subject)
            .whereEqualTo("target", target)
            .whereEqualTo("location", location)
            .whereEqualTo("date", date)
            .whereEqualTo("cost", cost)

        query.get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot.documents) {
                    val lessonDocumentRef = lessonsCollection.document(document.id)
                    lessonDocumentRef.update("booked", true, "clientId", clientId, "dateBooking", currentDate,
                        "timeBooking", currentTime, "canceled", false, "timeCancel", null, "dateCancel", null)
                        .addOnSuccessListener {
                            Toast.makeText(context, "Prenotazione effettuata con successo!", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(context, "Errore durante l'aggiornamento della prenotazione", Toast.LENGTH_SHORT).show()
                        }
                }
            }
            .addOnFailureListener {
                Toast.makeText(context, "Errore durante la ricerca", Toast.LENGTH_SHORT).show()
            }
    }

    fun isCostANumber(input: String): Boolean {
        val regex = """^\d+(\.\d+)?\s*€$""".toRegex()
        return regex.matches(input)
    }

    private fun isDateAfterOrToday(dateString: String): Boolean {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val inputDate = LocalDate.parse(dateString, formatter)

        val today = LocalDate.now()
        return !inputDate.isBefore(today)
    }
}
