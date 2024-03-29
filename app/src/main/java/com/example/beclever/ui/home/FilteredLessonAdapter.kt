package com.example.beclever.ui.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.beclever.R
import com.example.beclever.ui.plus.LessonModel
import com.example.beclever.ui.plus.LessonViewModel
import com.google.firebase.auth.FirebaseAuth
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Adapter per la visualizzazione delle lezioni filtrate.
 */
class FilteredLessonsAdapter(
    private val lessons: List<LessonModel>,
    private val lessonViewModel: LessonViewModel,
    private val lessonClickListener: LessonClickListener
) : RecyclerView.Adapter<FilteredLessonsAdapter.ViewHolder>() {

    /**
     * ViewHolder per ogni elemento della lista delle lezioni.
     */
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val usernameTextView: TextView = itemView.findViewById(R.id.textViewLessonUsernameSimple)
        val subjectTextView: TextView = itemView.findViewById(R.id.textViewLessonSubjectSimple)
        val dateTextView: TextView = itemView.findViewById(R.id.textViewLessonDateSimple)
        val hourTextView: TextView = itemView.findViewById(R.id.textViewLessonHourSimple)
        val targetTextView: TextView = itemView.findViewById(R.id.textViewLessonTargetSimple)
        val locationTextView: TextView = itemView.findViewById(R.id.textViewLessonLocationSimple)
        val costTextView: TextView = itemView.findViewById(R.id.textViewLessonCostSimple)
        val bookButton: Button = itemView.findViewById(R.id.prenotaButton)
    }

    /**
     * Funzione chiamata per creare un nuovo ViewHolder.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_lessons, parent, false)
        return ViewHolder(itemView)
    }

    /**
     * Funzione chiamata per collegare i dati a un ViewHolder specifico.
     */
    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val lesson = lessons[position]

        val currentUser = FirebaseAuth.getInstance().currentUser
        val clientId = currentUser?.uid

        if (!lesson.userId.isNullOrEmpty()) {
            lessonViewModel.fetchUserInfoAndSetUsername(lesson.userId, holder.usernameTextView)
            holder.subjectTextView.text = lesson.subject
            holder.dateTextView.text = lesson.date
            holder.hourTextView.text = lesson.hour
            holder.targetTextView.text = lesson.target
            holder.locationTextView.text = lesson.location
            holder.costTextView.text = lesson.cost

            if (lesson.isBooked) {
                holder.bookButton.text = "Prenotazione Effettuata"
                holder.bookButton.isEnabled = false // Disabilita il pulsante
            } else {
                if(lesson.userId == currentUser?.uid || !isDateAfterOrToday(lesson.date)) {
                    holder.bookButton.text = "Non prenotabile"
                    holder.bookButton.isEnabled = false
                    return
                }
                holder.bookButton.text = "Prenota"
                holder.bookButton.isEnabled = true // Abilita il pulsante
                holder.bookButton.setOnClickListener {
                    lesson.isBooked = true
                    if (clientId != null) {
                        lesson.clientId = clientId
                        lessonViewModel.updateLesson(lesson.userId, lesson.subject, lesson.target, lesson.location, lesson.date, lesson.cost, clientId, holder.itemView.context)
                        lessonClickListener.onLessonBooked(lesson)
                    }
                    notifyDataSetChanged()
                }
            }
        }
    }

    /**
     * Funzione chiamata per ottenere il numero di elementi nella lista.
     */
    override fun getItemCount(): Int = lessons.size

    /**
     * Interfaccia per la gestione degli eventi di prenotazione delle lezioni.
     */
    interface LessonClickListener {
        fun onLessonBooked(lesson: LessonModel)
    }

    /**
     * Verifica se una data è successiva a oggi o uguale a oggi.
     */
    fun isDateAfterOrToday(dateString: String): Boolean {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val inputDate = LocalDate.parse(dateString, formatter)

        val today = LocalDate.now()
        return !inputDate.isBefore(today)
    }
}