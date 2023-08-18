package com.example.beclever.ui.profile

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore

data class User(
    private var nome: String,
    private var email: String
)