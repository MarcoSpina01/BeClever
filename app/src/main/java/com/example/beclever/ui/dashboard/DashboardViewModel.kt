package com.example.beclever.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.beclever.ui.plus.Lesson
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class DashboardViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    private val _bookedLessonsLiveData: MutableLiveData<List<Lesson>> = MutableLiveData()
    private val _publishedLessonsLiveData: MutableLiveData<List<Lesson>> = MutableLiveData()

    val bookedLessonsLiveData: LiveData<List<Lesson>> = _bookedLessonsLiveData
    val publishedLessonsLiveData: LiveData<List<Lesson>> = _publishedLessonsLiveData

    init {
        loadLessons()
    }

    private fun loadLessons() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            db.collection("lessons")
                .get()
                .addOnSuccessListener { documents ->
                    val bookedLessons = mutableListOf<Lesson>()
                    val publishedLessons = mutableListOf<Lesson>()

                    for (doc in documents) {
                        val lesson = doc.toObject(Lesson::class.java)
                        if (lesson.clientId == userId) {
                            bookedLessons.add(lesson)
                        }
                        if (lesson.userId == userId) {
                            publishedLessons.add(lesson)
                        }
                    }

                    _bookedLessonsLiveData.value = bookedLessons
                    _publishedLessonsLiveData.value = publishedLessons
                }
        }
    }
}



