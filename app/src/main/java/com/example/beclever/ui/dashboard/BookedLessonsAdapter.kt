package com.example.beclever.ui.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.beclever.R
import com.example.beclever.ui.FilteredLessonsAdapter
import com.example.beclever.ui.plus.LessonModel

class BookedLessonsAdapter(
    private var bookedLessons: List<LessonModel>,
    private val dashboardViewModel: DashboardViewModel
    ) : RecyclerView.Adapter<BookedLessonsAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val usernameTextView: TextView = itemView.findViewById(R.id.textViewLessonUsernameBooked)
        val subjectTextView: TextView = itemView.findViewById(R.id.textViewLessonSubjectBooked)
        val dateTextView: TextView = itemView.findViewById(R.id.textViewLessonDateBooked)
        val targetTextView: TextView = itemView.findViewById(R.id.textViewLessonTargetBooked) // Aggiungi questa linea
        val locationTextView: TextView = itemView.findViewById(R.id.textViewLessonLocationBooked)
        val costTextView: TextView = itemView.findViewById(R.id.textViewLessonCostBooked) // Aggiungi questa linea
        //val bookButton: Button = itemView.findViewById(R.id.prenota)
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
            holder.targetTextView.text = currentLesson.target
            holder.locationTextView.text = currentLesson.location
            holder.costTextView.text = currentLesson.cost

        }
    }
    override fun getItemCount(): Int {
        return bookedLessons.size
    }

    fun updateData(newData: List<LessonModel>) {
        bookedLessons = newData
        notifyDataSetChanged()
    }
    // ... altri metodi di override
}




