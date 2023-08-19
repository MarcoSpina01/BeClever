package com.example.beclever.ui.plus

import androidx.lifecycle.ViewModel

class LessonViewModel : ViewModel() {

    private val lessonModel = LessonModel()

    //metodo per la creazione e storicizzazione di una nuova lezione
    fun createLesson(subject: String, date: String, target: String, location: String, cost: String, callback: (Boolean) -> Unit,) {
        lessonModel.createLesson(subject, date, target, location, cost) { success ->
            if (success) {
                callback(true)
            } else {
                callback(false)
            }
        }


    }

}