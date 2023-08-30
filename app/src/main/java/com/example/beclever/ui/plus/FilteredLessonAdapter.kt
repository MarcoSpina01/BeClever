package com.example.beclever.ui.plus

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.beclever.R
import com.example.beclever.ui.dashboard.DashboardViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class FilteredLessonsAdapter(
    private val lessons: List<LessonModel>,
    private val lessonViewModel: LessonViewModel,
    private val lessonClickListener: LessonClickListener
) : RecyclerView.Adapter<FilteredLessonsAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val usernameTextView: TextView = itemView.findViewById(R.id.textViewLessonUsernameSimple)
        val subjectTextView: TextView = itemView.findViewById(R.id.textViewLessonSubjectSimple)
        val dateTextView: TextView = itemView.findViewById(R.id.textViewLessonDateSimple)
        val targetTextView: TextView = itemView.findViewById(R.id.textViewLessonTargetSimple) // Aggiungi questa linea
        val locationTextView: TextView = itemView.findViewById(R.id.textViewLessonLocationSimple)
        val costTextView: TextView = itemView.findViewById(R.id.textViewLessonCostSimple) // Aggiungi questa linea
        val bookButton: Button = itemView.findViewById(R.id.prenotaButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_lessons, parent, false)
        return ViewHolder(itemView)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val lesson = lessons[position]

        val currentUser = FirebaseAuth.getInstance().currentUser
        val clientId = currentUser?.uid

        if (!lesson.userId.isNullOrEmpty()) {
            lessonViewModel.fetchUserInfoAndSetUsername(lesson.userId, holder.usernameTextView)
            holder.subjectTextView.text = lesson.subject
            holder.dateTextView.text = lesson.date
            holder.targetTextView.text = lesson.target // Aggiungi questa linea
            holder.locationTextView.text = lesson.location
            holder.costTextView.text = lesson.cost // Aggiungi questa linea
            if (lesson.isBooked) {
                holder.bookButton.text = "Prenotazione Effettuata"
                holder.bookButton.isEnabled = false // Disabilita il pulsante
            } else {
                if(lesson.userId == currentUser?.uid) {
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
    override fun getItemCount(): Int = lessons.size

    interface LessonClickListener {
        fun onLessonBooked(lesson: LessonModel)
    }
}