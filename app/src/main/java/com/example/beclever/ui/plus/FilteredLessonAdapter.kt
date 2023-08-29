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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class FilteredLessonsAdapter(
    private val lessons: List<LessonModel>,
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
            fetchUserInfoAndSetUsername(lesson.userId, holder.usernameTextView)
            holder.subjectTextView.text = " ${lesson.subject}"
            holder.dateTextView.text = "${lesson.date}"
            holder.targetTextView.text = " - ${lesson.target}" // Aggiungi questa linea
            holder.locationTextView.text = "${lesson.location}"
            holder.costTextView.text = "${lesson.cost}" // Aggiungi questa linea
            if (lesson.isBooked) {
                holder.bookButton.text = "Prenotazione Effettuata"
                holder.bookButton.isEnabled = false // Disabilita il pulsante
            } else {
                if(lesson.userId == currentUser?.uid) {
                    holder.bookButton.text = "Prenotazione non effettuabile"
                    holder.bookButton.isEnabled = false
                    return
                }
                holder.bookButton.text = "Prenota"
                holder.bookButton.isEnabled = true // Abilita il pulsante
                holder.bookButton.setOnClickListener {
                    lesson.isBooked = true
                    if (clientId != null) {
                        lessonClickListener.onLessonBooked(lesson)
                        updateFirestoreLesson(lesson.userId, lesson.subject, lesson.target, lesson.location, lesson.date, lesson.cost, clientId, holder.itemView.context)
                    }
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
            .addOnFailureListener {
            }
    }

    private fun updateFirestoreLesson(userId: String,subject: String, target: String, location: String, date: String, cost: String, clientId: String, context: Context) {
        val db = FirebaseFirestore.getInstance()
        val lessonsCollection = db.collection("lessons")

        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.BASIC_ISO_DATE
        val currentDate = current.format(formatter).toLong()

        val formatter2 = DateTimeFormatter.ISO_LOCAL_TIME
        val currentTime = current.format(formatter2).toString().replace(":", "").substringBefore(".").toLong()

        // Crea una query complessa che corrisponde a tutti i campi specificati
        val query = lessonsCollection
            .whereEqualTo("userId", userId)
            .whereEqualTo("subject", subject)
            .whereEqualTo("target", target)
            .whereEqualTo("location", location)
            .whereEqualTo("date", date)
            .whereEqualTo("cost", cost)

        query.get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot.documents) {
                    val lessonDocumentRef = lessonsCollection.document(document.id)
                    lessonDocumentRef.update("booked", true, "clientId", clientId, "dateBooking", currentDate,
                                                   "timeBooking", currentTime, "canceled", false, "timeCancel", null, "dateCancel", null)
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

    interface LessonClickListener {
        fun onLessonBooked(lesson: LessonModel)
    }
}