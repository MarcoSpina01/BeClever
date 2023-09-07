package com.example.beclever.ui.dashboard

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.beclever.R
import com.example.beclever.ui.plus.LessonModel

/**
 * Adapter per la visualizzazione delle lezioni pubblicate.
 */
class PublishedLessonsAdapter(

    private var publishedLessons: List<LessonModel>,
    private val dashboardViewModel: DashboardViewModel

) : RecyclerView.Adapter<PublishedLessonsAdapter.ViewHolder>() {

    /**
     * ViewHolder per ogni elemento della lista delle lezioni pubblicate.
     */
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val subjectTextView: TextView = itemView.findViewById(R.id.textViewLessonSubjectPublished)
        val dateTextView: TextView = itemView.findViewById(R.id.textViewLessonDatePublished)
        val hourTextView: TextView = itemView.findViewById(R.id.textViewLessonHourPublished)
        val targetTextView: TextView = itemView.findViewById(R.id.textViewLessonTargetPublished)
        val locationTextView: TextView = itemView.findViewById(R.id.textViewLessonLocationPublished)
        val costTextView: TextView = itemView.findViewById(R.id.textViewLessonCostPublished)
        val deleteButton: Button = itemView.findViewById(R.id.eliminaPublishedLesson)
    }

    /**
     * Funzione chiamata per creare un nuovo ViewHolder.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_publishedlessons, parent, false)
        return ViewHolder(itemView)
    }

    /**
     * Funzione chiamata per collegare i dati a un ViewHolder specifico.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentLesson = publishedLessons[position]

        if (!currentLesson.userId.isNullOrEmpty()) {

            holder.subjectTextView.text = currentLesson.subject
            holder.dateTextView.text = currentLesson.date
            holder.hourTextView.text = currentLesson.hour
            holder.targetTextView.text = currentLesson.target
            holder.locationTextView.text = currentLesson.location
            holder.costTextView.text = currentLesson.cost

            holder.deleteButton.setOnClickListener {
                val alertDialogBuilder = AlertDialog.Builder(holder.itemView.context)
                alertDialogBuilder.setTitle("Elimina Lezione")
                alertDialogBuilder.setMessage("Vuoi eliminare la lezione?")
                alertDialogBuilder.setPositiveButton("Si") { _, _ ->
                    dashboardViewModel.deleteLesson(currentLesson)
                }
                alertDialogBuilder.setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
                val alertDialog = alertDialogBuilder.create()
                alertDialog.show()
            }
        }
    }

    /**
     * Funzione chiamata per ottenere il numero di elementi nella lista.
     */
    override fun getItemCount(): Int {
        return publishedLessons.size
    }

    /**
     * Aggiorna i dati dell'adapter con una nuova lista di lezioni pubblicate.
     */
    fun updateData(newData: List<LessonModel>) {
        publishedLessons = newData
        notifyDataSetChanged()
    }
}
