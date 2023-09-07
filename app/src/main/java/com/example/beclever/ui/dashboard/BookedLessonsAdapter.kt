package com.example.beclever.ui.dashboard

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.beclever.R
import com.example.beclever.ui.notifications.NotificationsViewModel
import com.example.beclever.ui.plus.LessonModel

/**
 * Adapter per la RecyclerView delle lezioni prenotate.
 *
 * @param bookedLessons Lista delle lezioni prenotate.
 * @param dashboardViewModel ViewModel della dashboard.
 * @param notificationsViewModel ViewModel delle notifiche.
 */
class BookedLessonsAdapter(
    private var bookedLessons: List<LessonModel>,
    private val dashboardViewModel: DashboardViewModel,
    private val notificationsViewModel: NotificationsViewModel
) : RecyclerView.Adapter<BookedLessonsAdapter.ViewHolder>() {

    /**
     * ViewHolder per gli elementi della RecyclerView.
     */
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val usernameTextView: TextView = itemView.findViewById(R.id.textViewLessonUsernameBooked)
        val subjectTextView: TextView = itemView.findViewById(R.id.textViewLessonSubjectBooked)
        val dateTextView: TextView = itemView.findViewById(R.id.textViewLessonDateBooked)
        val hourTextView: TextView = itemView.findViewById(R.id.textViewLessonHourBooked)
        val targetTextView: TextView = itemView.findViewById(R.id.textViewLessonTargetBooked) // Aggiungi questa linea
        val locationTextView: TextView = itemView.findViewById(R.id.textViewLessonLocationBooked)
        val costTextView: TextView = itemView.findViewById(R.id.textViewLessonCostBooked) // Aggiungi questa linea
        val deleteBooking: TextView = itemView.findViewById(R.id.cancel_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_bookedlessons, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentLesson = bookedLessons[position]

        if (!currentLesson.userId.isNullOrEmpty()) {
            dashboardViewModel.fetchUserInfoAndSetUsername(
                currentLesson.userId,
                holder.usernameTextView
            )
            holder.subjectTextView.text = currentLesson.subject
            holder.dateTextView.text = currentLesson.date
            holder.hourTextView.text = currentLesson.hour
            holder.targetTextView.text = currentLesson.target
            holder.locationTextView.text = currentLesson.location
            holder.costTextView.text = currentLesson.cost

            holder.deleteBooking.setOnClickListener {
                val alertDialogBuilder = AlertDialog.Builder(holder.itemView.context)
                alertDialogBuilder.setTitle("Cancella Prenotazione")
                alertDialogBuilder.setMessage("Vuoi cancellare la prenotazione?")
                alertDialogBuilder.setPositiveButton("Si") { _, _ ->
                    dashboardViewModel.deleteBooking(currentLesson)
                    setNotification(currentLesson)
                }
                alertDialogBuilder.setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
                val alertDialog = alertDialogBuilder.create()
                alertDialog.show()
            }
        }
    }

    override fun getItemCount(): Int {
        return bookedLessons.size
    }

    /**
     * Aggiorna i dati dell'adapter con una nuova lista di lezioni prenotate.
     *
     * @param newData Nuova lista di lezioni prenotate.
     */
    fun updateData(newData: List<LessonModel>) {
        bookedLessons = newData
        notifyDataSetChanged()
    }

    /**
     * Crea una notifica quando una prenotazione viene cancellata.
     *
     * @param lesson Lezione cancellata.
     */
    private fun setNotification(lesson: LessonModel) {
        val message =
            "La prenotazione alla tua lezione di ${lesson.subject} del ${lesson.date} è stata cancellata"
        lesson.userId?.let {
            notificationsViewModel.createNotification(
                message,
                lesson.userId,
                lesson.clientId,
                lesson.lessonId
            ) { success ->
                if (success) {
                    // Aggiorna la UI o gestisci il successo
                } else {
                    // Gestisci l'errore nella creazione della notifica
                }
            }
        }
    }
}





