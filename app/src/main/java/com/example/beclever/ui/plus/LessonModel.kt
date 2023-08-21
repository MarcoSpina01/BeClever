package com.example.beclever.ui.plus

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class LessonModel {

    private lateinit var auth: FirebaseAuth

    private val db = Firebase.firestore

    fun createLesson(subject: String, date: String, target: String, location: String, cost: String, callback: (Boolean) -> Unit) {

        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid

        val lesson = Lesson(subject, date, target, location, cost, userId, false)

        db.collection("lessons")
            .add(lesson)
            .addOnSuccessListener {
                callback(true)
            }
            .addOnFailureListener {
                callback(false)
            }
    }
}