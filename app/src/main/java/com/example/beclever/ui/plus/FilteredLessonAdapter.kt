package com.example.beclever.ui

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
import com.example.beclever.ui.plus.LessonModel
import com.google.firebase.firestore.FirebaseFirestore

class FilteredLessonsAdapter(private val lessons: List<LessonModel>) :
    RecyclerView.Adapter<FilteredLessonsAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val usernameTextView: TextView = itemView.findViewById(R.id.textViewLessonUsername)
        val subjectTextView: TextView = itemView.findViewById(R.id.textViewLessonSubject)
        val dateTextView: TextView = itemView.findViewById(R.id.textViewLessonDate)
        val targetTextView: TextView = itemView.findViewById(R.id.textViewLessonTarget) // Aggiungi questa linea
        val locationTextView: TextView = itemView.findViewById(R.id.textViewLessonLocation)
        val costTextView: TextView = itemView.findViewById(R.id.textViewLessonCost) // Aggiungi questa linea
        val bookButton: Button = itemView.findViewById(R.id.prenota)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_lessons, parent, false)
        return ViewHolder(itemView)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val lesson = lessons[position]

        if (!lesson.userId.isNullOrEmpty()) {
            fetchUserInfoAndSetUsername(lesson.userId, holder.usernameTextView)
            holder.subjectTextView.text = " ${lesson.subject}"
            holder.dateTextView.text = " ${lesson.date}"
            holder.targetTextView.text = " ${lesson.target}" // Aggiungi questa linea
            holder.locationTextView.text = " ${lesson.location}"
            holder.costTextView.text = " ${lesson.cost}" // Aggiungi questa linea
            if (lesson.isBooked) {
                holder.bookButton.text = "Prenotazione Effettuata"
                holder.bookButton.isEnabled = false // Disabilita il pulsante
            } else {
                holder.bookButton.text = "Prenota"
                holder.bookButton.isEnabled = true // Abilita il pulsante
                holder.bookButton.setOnClickListener {
                    lesson.isBooked = true
                    updateFirestoreBookingStatus(lesson.userId, lesson.subject, lesson.target, lesson.location, lesson.date, lesson.cost, holder.itemView.context)
                    notifyDataSetChanged()
                }
            }
        }
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
                //val errorMessage = "Errore durante il recupero delle informazioni dell'utente: ${e.message}"
                //Toast.makeText(usernameTextView.context, errorMessage, Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateFirestoreBookingStatus(lessonId: String,subject: String, target: String, location: String, date: String, cost: String, context: Context) {
        val db = FirebaseFirestore.getInstance()
        val lessonsCollection = db.collection("lessons")

        // Crea una query complessa che corrisponde a tutti i campi specificati
        val query = lessonsCollection
            .whereEqualTo("subject", subject)
            .whereEqualTo("target", target)
            .whereEqualTo("location", location)
            .whereEqualTo("date", date)
            .whereEqualTo("cost", cost)

        query.get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot.documents) {
                    val lessonDocumentRef = lessonsCollection.document(document.id)
                    lessonDocumentRef.update("booked", true)
                        .addOnSuccessListener {
                            Toast.makeText(context, "Prenotazione effettuata con successo!", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(context, "Errore durante l'aggiornamento della prenotazione", Toast.LENGTH_SHORT).show()
                        }
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Errore durante la ricerca", Toast.LENGTH_SHORT).show()
            }
    }

    override fun getItemCount(): Int = lessons.size
}