package com.example.beclever.ui.plus

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.icu.util.Calendar
import android.text.Editable
import android.text.TextWatcher
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.beclever.R
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.ArrayList

class PlusViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is settings Fragment"
    }
    val text: LiveData<String> = _text

    private val db = Firebase.firestore


    //metodo per la creazione e storicizzazione di una nuova lezione
    fun createLesson(subject: String, date: String, target: String, location: String, cost: String) {
        val lesson = Lesson(subject, date, target, location, cost)

        db.collection("lessons")
            .add(lesson)
            .addOnSuccessListener {
               // Toast.makeText(requireContext(), "Lezione creata con successo!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
               // Toast.makeText(requireContext(), "Si è verificato un errore durante la creazione della lezione", Toast.LENGTH_SHORT).show()
            }
    }

}