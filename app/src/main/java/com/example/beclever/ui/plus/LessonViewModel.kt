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

/**
 * ViewModel per la gestione delle lezioni.
 */
class LessonViewModel : ViewModel() {

    // Firebase Firestore per l'accesso al database Firestore
    private val db = Firebase.firestore

    /**
     * Crea una nuova lezione nel database Firestore.
     *
     * @param subject Il soggetto della lezione.
     * @param date La data della lezione nel formato "dd/MM/yyyy".
     * @param target Il target della lezione.
     * @param location La posizione della lezione.
     * @param cost Il costo della lezione nel formato "numero €".
     * @param hour L'orario della lezione.
     * @param callback Una funzione di callback per gestire l'esito dell'operazione.
     */
    fun createLesson(subject: String, date: String, target: String, location: String, cost: String, hour: String, callback: (Boolean) -> Unit) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid

        // Verifica che la data sia valida (non precedente alla data attuale)
        if(!isDateAfterOrToday(date)) {
            callback(false)
            return
        }

        // Verifica che il costo sia un numero valido
        if(!isCostANumber(cost)) {
            callback(false)
            return
        }

        // Crea un oggetto LessonModel con i dati della lezione
        val lessonModel = LessonModel(subject, date, target, location, cost, hour, userId, false, "", "", null, null, false, null, null)

        // Aggiunge la lezione al database Firestore
        db.collection("lessons")
            .add(lessonModel)
            .addOnSuccessListener { documentReference ->
                val lessonId = documentReference.id // Ottieni il nome del documento generato da Firebase

                // Aggiorna il campo lessonId nel documento con il nome del documento stesso
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

    /**
     * Ottiene le informazioni dell'utente e imposta il nome utente in una TextView.
     *
     * @param userId L'ID dell'utente di cui ottenere le informazioni.
     * @param usernameTextView La TextView in cui impostare il nome utente.
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
            }
    }

    /**
     * Aggiorna una lezione nel database Firestore con lo stato di prenotazione.
     *
     * @param userId L'ID dell'utente che effettua la prenotazione.
     * @param subject Il soggetto della lezione.
     * @param target Il target della lezione.
     * @param location La posizione della lezione.
     * @param date La data della lezione nel formato "dd/MM/yyyy".
     * @param cost Il costo della lezione nel formato "numero €".
     * @param clientId L'ID del cliente che effettua la prenotazione.
     * @param context Il contesto dell'applicazione.
     */
    fun updateLesson(userId: String,subject: String, target: String, location: String, date: String, cost: String, clientId: String, context: Context) {
        val db = FirebaseFirestore.getInstance()
        val lessonsCollection = db.collection("lessons")

        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.BASIC_ISO_DATE
        val currentDate = current.format(formatter).toLong()

        val formatter2 = DateTimeFormatter.ISO_LOCAL_TIME
        val currentTime = current.format(formatter2).toString().replace(":", "").substringBefore(".").toLong()

        // Crea una query per trovare la lezione da aggiornare
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

    /**
     * Verifica se una stringa rappresenta un costo valido (numero seguito da €).
     *
     * @param input La stringa da verificare.
     * @return True se la stringa è un costo valido, altrimenti False.
     */
    fun isCostANumber(input: String): Boolean {
        val regex = """^\d+(\.\d+)?\s*€$""".toRegex()
        return regex.matches(input)
    }

    /**
     * Verifica se una data è successiva o uguale alla data attuale.
     *
     * @param dateString La data da verificare nel formato "dd/MM/yyyy".
     * @return True se la data è successiva o uguale alla data attuale, altrimenti False.
     */
    private fun isDateAfterOrToday(dateString: String): Boolean {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val inputDate = LocalDate.parse(dateString, formatter)

        val today = LocalDate.now()
        return !inputDate.isBefore(today)
    }
}
