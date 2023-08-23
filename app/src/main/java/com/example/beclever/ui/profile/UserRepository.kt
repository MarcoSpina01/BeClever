package com.example.beclever.ui.profile

import android.content.ContentValues.TAG
import android.util.Log
import com.example.beclever.ui.login.RegistrationCallback
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging


class UserRepository {

    private lateinit var auth: FirebaseAuth
    private var token = ""

    fun fetchUserData(onDataFetched: (UserModel?) -> Unit) {
        getData { data ->
            if (data != null) {

                val nome = data["first"] as String
                val email = data["email"] as String
                val cognome = data["last"] as String
                val bio = data["bio"] as String
                val qualifica = data["qualification"] as String
                val notificationId = data["notificationId"] as String
                val userModel = UserModel(nome, email, cognome, bio, qualifica, notificationId, "")



                onDataFetched(userModel)
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

    private fun reAuthenticateUser(oldPassword: String, onComplete: (Boolean) -> Unit) {
        val user = Firebase.auth.currentUser
        val email = user?.email
        val credential = email?.let { EmailAuthProvider.getCredential(it, oldPassword) }

        if (credential != null) {
            user.reauthenticate(credential)
                .addOnCompleteListener { task ->
                    val success = task.isSuccessful
                    onComplete(success) // Chiamato con true se l'operazione è riuscita, altrimenti false
                }
        } else {
            onComplete(false) // Se non è stato possibile ottenere le credenziali o l'utente
        }
    }

    fun updatePassword(
        oldPassword: String,
        newPassword: String,
    ): Boolean {
        var result = true
        reAuthenticateUser(oldPassword) { success ->
            if (!success) {
                // Reautenticazione fallita
                Log.d(TAG, "User re-authentication failed.")
                result = false
            }
        }

        if(result) {
            val user = Firebase.auth.currentUser
            user!!.updatePassword(newPassword)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                    }
                }
        }

        return result
    }

    private fun setNotificationToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                token = task.result
            }
        }
    }

    fun storeUser(userModel: UserModel, callback: RegistrationCallback) {
        auth = FirebaseAuth.getInstance()
        setNotificationToken()
        auth.createUserWithEmailAndPassword(userModel.getEmail(), userModel.getPassword())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val db = Firebase.firestore
                    val userId = user?.uid
                    if (userId != null) {
                        val userMap = hashMapOf(
                            "first" to userModel.getNome(),
                            "last" to userModel.getCognome(),
                            "email" to userModel.getEmail(),
                            "bio" to "",
                            "qualification" to "",
                            "uid" to userId,
                            "notificationId" to token
                        )
                        db.collection("users")
                            .document(userId)
                            .set(userMap)
                            .addOnSuccessListener {
                                // Il documento è stato creato con successo
                                callback.onRegistrationSuccess(user)
                            }
                            .addOnFailureListener { e ->
                                // Si è verificato un errore durante la creazione del documento
                                callback.onRegistrationFailure(e)
                            }
                    }
                } else {
                    // Si è verificato un errore durante la registrazione
                    callback.onRegistrationFailure(task.exception!!)
                }
            }
    }


}

