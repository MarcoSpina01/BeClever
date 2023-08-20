package com.example.beclever.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.beclever.R
import com.example.beclever.ui.plus.Lesson

class FilteredLessonsAdapter(private val lessons: List<Lesson>) :
    RecyclerView.Adapter<FilteredLessonsAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val subjectTextView: TextView = itemView.findViewById(R.id.textViewLessonSubject)
        val dateTextView: TextView = itemView.findViewById(R.id.textViewLessonDate)
        val targetTextView: TextView = itemView.findViewById(R.id.textViewLessonTarget) // Aggiungi questa linea
        val locationTextView: TextView = itemView.findViewById(R.id.textViewLessonLocation)
        val costTextView: TextView = itemView.findViewById(R.id.textViewLessonCost) // Aggiungi questa linea
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_lessons, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val lesson = lessons[position]
        holder.subjectTextView.text = "Materia : ${lesson.subject}"
        holder.dateTextView.text = "Data : ${lesson.date}"
        holder.targetTextView.text = "Target : ${lesson.target}" // Aggiungi questa linea
        holder.locationTextView.text = "Provincia : ${lesson.location}"
        holder.costTextView.text = "Prezzo : ${lesson.cost}" // Aggiungi questa linea
    }

    override fun getItemCount(): Int = lessons.size
}