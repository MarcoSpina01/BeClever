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

class NotificationsViewModel : ViewModel() {

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val _notificationsLiveData: MutableLiveData<List<Notification>> = MutableLiveData()
    val notificationsLiveData: LiveData<List<Notification>> = _notificationsLiveData

    val currentUser = FirebaseAuth.getInstance().currentUser
    val userId = currentUser?.uid

    private val db = Firebase.firestore

    init {
        if (userId != null) {
            loadNotificationsFromFirebase(userId)
        }
    }

    fun createNotification(message: String, userId: String, clientId: String?, lessonId: String, callback: (Boolean) -> Unit){

        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.BASIC_ISO_DATE
        val currentDate = current.format(formatter).toLong()

        val formatter2 = DateTimeFormatter.ISO_LOCAL_TIME
        val currentTime = current.format(formatter2).toString().replace(":", "").substringBefore(".").toLong()

        val notificationModel = Notification()

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

    private fun loadNotificationsFromFirebase(userId: String) {
        firestore.collection("notifications")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { querySnapshot: QuerySnapshot ->
                val notificationsList = mutableListOf<Notification>()
                for (document in querySnapshot.documents) {
                    val message = document.getString("message")
                    val date = document.getLong("date")
                    val time = document.getLong("time")
                    val userId = document.getString("userId")
                    val lessonId = document.getString("lessonId")
                    val clientId = document.getString("clientId")
                    if (message != null && userId != null) {
                        val notification = Notification(message, date, time, userId, clientId, lessonId)
                        notificationsList.add(notification)
                    }
                }
                notificationsList.sortWith(compareBy( {it.date}, {it.time}) )
                _notificationsLiveData.postValue(notificationsList.reversed())
            }
            .addOnFailureListener {
                // Handle failure (e.g., show an error message)
            }
    }


}