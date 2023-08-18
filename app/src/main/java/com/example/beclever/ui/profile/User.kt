package com.example.beclever.ui.profile

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore

data class User(
     var nome: String,
     var email: String
) {
     // Costruttore senza argomenti richiesto da Firebase Firestore
     constructor() : this("", "") // Aggiungi altri campi se necessario
}
    /*fun getNome(): String {
        return nome
    }

    fun getEmail(): String {
        return email
    }*/

