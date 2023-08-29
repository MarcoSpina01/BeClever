package com.example.beclever.ui.notifications

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.beclever.R
import com.google.firebase.auth.FirebaseAuth

class NotificationsAdapter(
    private var notifications: List<Notification>

)  : RecyclerView.Adapter<NotificationsAdapter.ViewHolder>(){


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val notificationText: TextView = itemView.findViewById(R.id.notificationText)
        val notificationTime: TextView = itemView.findViewById(R.id.notificationTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_notification, parent, false)
        return ViewHolder(itemView)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentnotification = notifications[position]

        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid

        if (!currentnotification.userId.isNullOrEmpty()) {

            holder.notificationText.text = "${currentnotification.message}"
            holder.notificationTime.text = "${currentnotification.getTempoPassato()}"
        }
    }

    override fun getItemCount(): Int {
        return notifications.size
    }

    fun updateData(newData: List<Notification>) {
        notifications = newData
        notifyDataSetChanged()
    }
}


