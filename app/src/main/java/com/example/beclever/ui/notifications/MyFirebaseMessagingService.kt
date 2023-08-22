package com.example.beclever.ui.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import androidx.core.app.NotificationCompat
import com.example.beclever.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // Questo metodo viene chiamato quando una notifica push viene ricevuta mentre l'app è in primo piano
        // Puoi personalizzare la gestione delle notifiche qui

        // Estrai i dati dalla notifica
        val title = remoteMessage.notification?.title
        val body = remoteMessage.notification?.body
        val data = remoteMessage.data

        // Visualizza la notifica personalizzata
        showNotification(title, body, data)
    }

    private fun showNotification(title: String?, body: String?, data: Map<String, String>) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "channel_id"
        val channelName = "Channel Name"

        // Crea il canale di notifica (solo per Android Oreo e versioni successive)
        val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
        channel.lightColor = Color.GREEN
        channel.enableVibration(true)
        notificationManager.createNotificationChannel(channel)

        // Crea la notifica
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.drawable.beclever_secondologo_hr_blue) // Icona da utilizzare nella notifica
            .setAutoCancel(true)

        // Costruisci e mostra la notifica
        val notification = notificationBuilder.build()
        notificationManager.notify(0, notification)
    }

    override fun onNewToken(token: String) {
        // Questo metodo viene chiamato quando il token FCM dell'utente cambia
        // Puoi aggiornare il token nel tuo server o fare altre azioni necessarie
    }
}