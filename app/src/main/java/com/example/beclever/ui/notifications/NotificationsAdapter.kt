package com.example.beclever.ui.notifications

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.beclever.R
import com.google.firebase.auth.FirebaseAuth

/**
 * Adapter per visualizzare le notifiche in un RecyclerView.
 *
 * @property notificationModels Lista delle notifiche da visualizzare nell'adapter.
 */
class NotificationsAdapter(
    private var notificationModels: List<NotificationModel>
) : RecyclerView.Adapter<NotificationsAdapter.ViewHolder>() {

    /**
     * ViewHolder per visualizzare gli elementi delle notifiche.
     *
     * @param itemView La vista dell'elemento dell'adapter.
     */
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val notificationText: TextView = itemView.findViewById(R.id.notificationText)
        val notificationTime: TextView = itemView.findViewById(R.id.notificationTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_notification, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentNotification = notificationModels[position]

        // Verifica se l'ID dell'utente è presente nella notifica
        if (!currentNotification.userId.isNullOrEmpty()) {
            // Imposta il testo della notifica e il tempo trascorso dalla notifica
            holder.notificationText.text = currentNotification.message
            holder.notificationTime.text = currentNotification.getTempoPassato()
        }
    }

    override fun getItemCount(): Int {
        return notificationModels.size
    }

    /**
     * Aggiorna i dati nell'adapter con una nuova lista di notifiche.
     *
     * @param newData La nuova lista di notifiche da visualizzare.
     */
    fun updateData(newData: List<NotificationModel>) {
        notificationModels = newData
        notifyDataSetChanged()
    }
}


