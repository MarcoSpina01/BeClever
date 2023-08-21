package com.example.beclever.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.beclever.R
import com.example.beclever.ui.plus.Lesson
import com.google.firebase.firestore.FirebaseFirestore

class FilteredLessonsAdapter(private val lessons: List<Lesson>) :
    RecyclerView.Adapter<FilteredLessonsAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val usernameTextView: TextView = itemView.findViewById(R.id.textViewLessonUsername)
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

        if (!lesson.userId.isNullOrEmpty()) {
            fetchUserInfoAndSetUsername(lesson.userId, holder.usernameTextView)
        }
        holder.subjectTextView.text = " ${lesson.subject}"
        holder.dateTextView.text = " ${lesson.date}"
        holder.targetTextView.text = " ${lesson.target}" // Aggiungi questa linea
        holder.locationTextView.text = " ${lesson.location}"
        holder.costTextView.text = " ${lesson.cost}" // Aggiungi questa linea
    }

    private fun fetchUserInfoAndSetUsername(userId: String, usernameTextView: TextView) {
        val db = FirebaseFirestore.getInstance()
        val usersCollection = db.collection("users")

        usersCollection.document(userId).get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val userName = documentSnapshot.getString("first") ?: ""
                    usernameTextView.text = " ${userName}"
                }
            }
            .addOnFailureListener { e ->
                val errorMessage = "Errore durante il recupero delle informazioni dell'utente: ${e.message}"
                Toast.makeText(usernameTextView.context, errorMessage, Toast.LENGTH_SHORT).show()
            }
    }

    override fun getItemCount(): Int = lessons.size
}