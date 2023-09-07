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

/**
 * Repository per la gestione dei dati dell'utente, inclusa l'interazione con Firebase Authentication e Firestore.
 *
 * Questa classe fornisce metodi per recuperare, aggiornare e memorizzare i dati dell'utente,
 * oltre a gestire l'autenticazione e le operazioni relative alla password.
 *
 * @property auth Un'istanza di FirebaseAuth per gestire l'autenticazione.
 * @property token Il token delle notifiche dell'utente.
 */
class UserRepository {

    private lateinit var auth: FirebaseAuth
    private var token = ""

    /**
     * Recupera i dati dell'utente dal Firestore e li converte in un oggetto [UserModel].
     *
     * @param onDataFetched La callback chiamata quando i dati dell'utente sono stati recuperati con successo.
     */
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
                onDataFetched(null)
            }
        }

    }

    /**
     * Recupera i dati dell'utente dal Firestore.
     *
     * @param callback La callback chiamata con i dati recuperati o null in caso di errore.
     */
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

    /**
     * Aggiorna il profilo dell'utente nel Firestore e l'indirizzo email nell'autenticazione Firebase.
     *
     * @param userId L'ID dell'utente da aggiornare.
     * @param newName Il nuovo nome dell'utente.
     * @param newSurname Il nuovo cognome dell'utente.
     * @param newEmail Il nuovo indirizzo email dell'utente.
     * @param newBio La nuova biografia o descrizione dell'utente.
     * @param newQualification La nuova qualifica o titolo dell'utente.
     * @return Una [Task] che rappresenta lo stato dell'operazione.
     */
    fun updateUserProfile(userId: String, newName: String, newSurname: String, newEmail: String, newBio: String, newQualification: String): Task<Void> {

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

    /**
     * Reautentica l'utente corrente utilizzando la vecchia password.
     *
     * @param oldPassword La vecchia password dell'utente.
     * @param onComplete Una callback chiamata con il risultato dell'operazione di reautenticazione (true se riuscita, altrimenti false).
     */
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

    /**
     * Aggiorna la password dell'utente corrente.
     *
     * @param oldPassword La vecchia password dell'utente per la reautenticazione.
     * @param newPassword La nuova password da impostare per l'utente.
     * @return true se l'aggiornamento della password è riuscito, altrimenti false.
     */
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
                .addOnCompleteListener {
                }
        }

        return result
    }

    /**
     * Memorizza il token delle notifiche dell'utente.
     */
    private fun setNotificationToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                token = task.result
            }
        }
    }

    /**
     * Registra un nuovo utente con Firebase Authentication e memorizza i dati correlati nel Firestore.
     *
     * @param userModel Il modello dati dell'utente da registrare.
     * @param callback La callback chiamata in caso di successo o fallimento della registrazione.
     */
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

