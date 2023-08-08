package com.example.beclever.ui.profile

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore

class ProfileRepository {

    private lateinit var auth: FirebaseAuth

    fun getData(callback: (Map<String, Any>?) -> Unit) {
        auth = FirebaseAuth.getInstance()
        val id = auth.currentUser?.uid
        val collectionRef = FirebaseFirestore.getInstance().collection("users")

        val query = collectionRef.whereEqualTo(FieldPath.documentId(), id)

        query.get()
            .addOnSuccessListener { querySnapshot ->
                // Elabora i risultati della query qui
                for (documentSnapshot in querySnapshot) {
                    val data = documentSnapshot.data
                    callback(data) // Pass the data to the callback
                    return@addOnSuccessListener // Exit the loop after the first result (if there are multiple)
                }
                callback(null) // If no matching document is found, pass null to the callback
            }
            .addOnFailureListener { exception ->
                // Gestisci eventuali errori qui
                callback(null) // Pass null to the callback in case of failure
            }
    }
}