package com.example.beclever.ui.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * ViewModel per la gestione delle notifiche dell'utente.
 *
 * @property firestore Oggetto Firestore per l'accesso al database Firebase.
 * @property _notificationsLiveData LiveData privato che tiene traccia della lista delle notifiche.
 * @property notificationsLiveData LiveData pubblico per l'osservazione delle notifiche.
 * @property currentUser Oggetto che rappresenta l'utente corrente.
 * @property userId ID dell'utente corrente.
 * @property db Oggetto Firestore per l'accesso semplificato al database Firebase.
 */
class NotificationsViewModel : ViewModel() {

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val _notificationsLiveData: MutableLiveData<List<NotificationModel>> = MutableLiveData()
    val notificationsLiveData: LiveData<List<NotificationModel>> = _notificationsLiveData

    private val currentUser = FirebaseAuth.getInstance().currentUser
    val userId = currentUser?.uid


    private val db = Firebase.firestore

    /**
     * Costruttore inizializza il caricamento delle notifiche se l'ID dell'utente è disponibile.
     */
    init {
        if (userId != null) {
            loadNotificationsFromFirebase(userId)
        }
    }

    /**
     * Crea una nuova notifica nel database Firebase.
     *
     * @param message Il testo della notifica.
     * @param userId ID dell'utente a cui è destinata la notifica.
     * @param clientId ID del cliente (opzionale).
     * @param lessonId ID della lezione associata alla notifica.
     * @param callback Callback chiamata al termine dell'operazione con un valore booleano per il successo o il fallimento.
     */
    fun createNotification(
        message: String,
        userId: String,
        clientId: String?,
        lessonId: String,
        callback: (Boolean) -> Unit
    ) {

        // Ottieni la data e l'orario correnti
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.BASIC_ISO_DATE
        val currentDate = current.format(formatter).toLong()

        val formatter2 = DateTimeFormatter.ISO_LOCAL_TIME
        val currentTime = current.format(formatter2).toString().replace(":", "").substringBefore(".").toLong()

        val notificationModel = NotificationModel()

        db.collection("notifications")
            .add(notificationModel)
            .addOnSuccessListener { documentReference ->
                val notificationId = documentReference.id // Ottieni il nome del documento generato da Firebase
                // Ora aggiorna il campo lessonId nel documento con il nome del documento stesso
                db.collection("notifications")
                    .document(notificationId)
                    .update("notificationId", notificationId, "message", message, "userId", userId, "lessonId", lessonId,
                                "date", currentDate, "time", currentTime, "clientId", clientId)
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
     * Carica le notifiche dell'utente dal database Firebase.
     *
     * @param userId ID dell'utente di cui caricare le notifiche.
     */
    fun loadNotificationsFromFirebase(userId: String) {
        firestore.collection("notifications")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { querySnapshot: QuerySnapshot ->
                val notificationsList = mutableListOf<NotificationModel>()
                for (document in querySnapshot.documents) {
                    val message = document.getString("message")
                    val date = document.getLong("date")
                    val time = document.getLong("time")
                    val userId = document.getString("userId")
                    val lessonId = document.getString("lessonId")
                    val clientId = document.getString("clientId")
                    if (message != null && userId != null) {
                        val notificationModel = NotificationModel(message, date, time, userId, clientId, lessonId)
                        notificationsList.add(notificationModel)
                    }
                }
                notificationsList.sortWith(compareBy( {it.date}, {it.time}) )
                _notificationsLiveData.postValue(notificationsList.reversed())
            }
    }


}