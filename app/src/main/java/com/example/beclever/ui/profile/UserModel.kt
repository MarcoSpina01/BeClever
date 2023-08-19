package com.example.beclever.ui.profile

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore

class UserModel {

    private lateinit var auth: FirebaseAuth

    fun fetchUserData(onDataFetched: (User?) -> Unit) {
        getData { data ->
            if (data != null) {
                val nome = data["first"] as String
                val email = data["email"] as String
                val cognome = data["last"] as String
                val bio = data["bio"] as String
                val qualifica = data["qualification"] as String
                val user = User(nome, email, cognome, bio, qualifica)
                onDataFetched(user)
            } else {
                // fai qualcosa in caso contrario
            }
        }

    }


    private fun getData(callback: (Map<String, Any>?) -> Unit) {
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
            .addOnFailureListener {
                // Gestisci eventuali errori qui
                callback(null) // Pass null to the callback in case of failure
            }
    }

    fun updateUserProfile(userId: String, newName: String, newSurname: String, newEmail: String, newBio: String, newQualification: String): Task<Void> {

        /*val db = FirebaseFirestore.getInstance() // Inizializza db qui o altrove se preferisci

        val userRef = db.collection("users").document(userId)
        return userRef.update("first", newName, "last", newSurname, "email", newEmail, "bio", newBio, "qualification", newQualification )*/

        val db = FirebaseFirestore.getInstance()
        val userRef = db.collection("users").document(userId)
        val user = FirebaseAuth.getInstance().currentUser

        if (user != null) {
            val updateEmailTask = user.updateEmail(newEmail)
            return updateEmailTask.continueWithTask { task ->
                if (task.isSuccessful) {
                    // L'aggiornamento dell'email nell'autenticazione Firebase è riuscito
                    // Ora puoi aggiornare l'email nell'archivio dati Firestore
                    userRef.update(
                        "first", newName,
                        "last", newSurname,
                        "email", newEmail,
                        "bio", newBio,
                        "qualification", newQualification
                    )
                } else {
                    // L'aggiornamento dell'email nell'autenticazione Firebase è fallito
                    // Puoi gestire l'errore qui se necessario
                    throw task.exception ?: Exception("Update email failed")
                }
            }
        } else {
            throw Exception("User not authenticated")
        }
    }

}