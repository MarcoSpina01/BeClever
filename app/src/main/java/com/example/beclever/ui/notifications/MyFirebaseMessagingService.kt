package com.example.beclever.ui.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color

import androidx.core.app.NotificationCompat
import com.example.beclever.ui.home.HomeActivity
import com.example.beclever.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

/**
 * Servizio per la gestione delle notifiche push tramite Firebase Cloud Messaging (FCM).
 */
class MyFirebaseMessagingService : FirebaseMessagingService() {

    /**
     * Callback chiamato quando viene ricevuta una notifica push.
     *
     * @param remoteMessage Oggetto che rappresenta i dati della notifica ricevuta.
     */
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val title = remoteMessage.notification?.title
        val body = remoteMessage.notification?.body
        val data = remoteMessage.data

        // Intent per l'azione da eseguire quando l'utente fa clic sulla notifica
        val intent = Intent(this, HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "channel_id"
        val channelName = "Channel Name"

        val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
        channel.lightColor = Color.GREEN
        channel.enableVibration(true)
        notificationManager.createNotificationChannel(channel)

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.drawable.beclever_secondologo_hr_blue)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent) // Aggiungi l'intent all'azione del clic

        val notification = notificationBuilder.build()
        notificationManager.notify(0, notification)
    }

}