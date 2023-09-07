package com.example.beclever.ui.profile

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.beclever.ui.login.RegistrationCallback
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage


/**
 * ViewModel per la gestione dei dati utente e delle operazioni associate come il recupero, l'aggiornamento e la registrazione dell'utente.
 *
 * @property userRepository Il repository per le operazioni utente.
 * @property _userModel Il LiveData contenente i dati dell'utente.
 * @property userModel Il LiveData pubblico per i dati dell'utente.
 */
class UserViewModel : ViewModel() {


    private val userRepository = UserRepository()

    private val _userModel = MutableLiveData<UserModel>()
    val userModel: LiveData<UserModel>
        get() = _userModel

    /**
     * Recupera i dati dell'utente e li imposta nel LiveData [_userModel].
     */
    fun fetchUserData() {
        userRepository.fetchUserData { user ->
            if (user != null) {
                _userModel.value = user
            } else {
                // Nessun dato corrispondente trovato
            }
        }
    }

    /**
     * Aggiorna il profilo dell'utente con i nuovi dati forniti.
     *
     * @param userId ID dell'utente.
     * @param newName Nuovo nome dell'utente.
     * @param newSurname Nuovo cognome dell'utente.
     * @param newEmail Nuova email dell'utente.
     * @param newBio Nuova biografia dell'utente.
     * @param newQualification Nuova qualifica dell'utente.
     * @param context Contesto dell'applicazione.
     */
    fun updateUserProfile(userId: String, newName: String, newSurname: String, newEmail: String, newBio: String, newQualification: String, context: Context?) {userRepository.updateUserProfile(userId, newName, newSurname, newEmail, newBio, newQualification)
        .addOnSuccessListener {
            Toast.makeText(context, "Profilo aggiornato con successo!", Toast.LENGTH_SHORT).show()
            fetchUserData()
        }
        .addOnFailureListener {
            Toast.makeText(context, "Errore durante la modifica", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Aggiorna la password dell'utente.
     *
     * @param oldPassword Vecchia password dell'utente per la reautenticazione.
     * @param newPassword Nuova password da impostare per l'utente.
     * @param onComplete Callback chiamata con il risultato dell'aggiornamento.
     */
    fun updatePassword(
        oldPassword: String,
        newPassword: String,
        onComplete: (ChangePasswordResult) -> Unit
    ) {
        when {
            newPassword == oldPassword -> onComplete(ChangePasswordResult.NEW_PASSWORD_DIFFERENT)
            newPassword.length < 8 -> onComplete(ChangePasswordResult.NEW_PASSWORD_TOO_SHORT)
            userRepository.updatePassword(oldPassword, newPassword) -> onComplete(ChangePasswordResult.SUCCESS)
            !userRepository.updatePassword(oldPassword, newPassword) -> onComplete(ChangePasswordResult.CURRENT_PASSWORD_INCORRECT)
            else -> onComplete(ChangePasswordResult.ERRORE)
        }
    }

    /**
     * Registra un nuovo utente con le informazioni fornite.
     *
     * @param email Email dell'utente.
     * @param password Password dell'utente.
     * @param nome Nome dell'utente.
     * @param cognome Cognome dell'utente.
     * @param callback Callback per il risultato della registrazione.
     */
    fun registerUser(email: String, password: String, nome: String, cognome: String, callback: RegistrationCallback) {
        val user = UserModel(nome, email, cognome, "", "", "", password)
        userRepository.storeUser(user, callback)
    }

    /**
     * Verifica se i dati di registrazione dell'utente sono validi.
     *
     * @param user I dati dell'utente da verificare.
     * @return true se i dati sono validi, altrimenti false.
     */
    fun isRegistrationDataValid(user: UserModel): Boolean {
        return user.isNomeValid && user.isCognomeValid && user.isEmailValid && user.isPasswordValid
    }


    // Funzione per aggiornare l'URL dell'immagine del profilo nel database Firestore
    /**
     * Aggiorna l'URL dell'immagine del profilo dell'utente nel database Firestore.
     *
     * @param userId L'ID dell'utente.
     * @param imageUrl L'URL dell'immagine da aggiornare.
     * @param successCallback Callback chiamata in caso di successo.
     * @param errorCallback Callback chiamata in caso di errore.
     */
    fun updateUserProfileImage(userId: String, imageUrl: String, successCallback: () -> Unit, errorCallback: (Exception) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val userRef = db.collection("users").document(userId)

        val data = hashMapOf(
            "profileImage" to imageUrl
        )

        userRef.update(data as Map<String, Any>)
            .addOnSuccessListener {
                // L'aggiornamento è andato a buon fine
                successCallback.invoke()
            }
            .addOnFailureListener { e ->
                // Si è verificato un errore durante l'aggiornamento
                errorCallback.invoke(e)
            }
    }

    /**
     * Recupera l'URL dell'immagine del profilo dell'utente dal database Firestore.
     *
     * @param callback Callback che riceve l'URL dell'immagine.
     */
    fun fetchUserImage(callback: (String?) -> Unit) {
        // Ottieni l'ID dell'utente corrente da Firebase
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid

        if (userId != null) {
            val userDocumentRef =
                FirebaseFirestore.getInstance().collection("users").document(userId)

            userDocumentRef.get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        // Ottieni l'URL dell'immagine del profilo dal documento Firestore
                        val imageUrl = documentSnapshot.getString("profileImage")
                        callback(imageUrl) // Passa l'URL all'interno della callback
                    } else {
                        callback(null) // Nessun documento trovato, passa null
                    }
                }
                .addOnFailureListener {
                    callback(null) // Gestisce eventuali errori, passa null
                }
        } else {
            callback(null) // Nessun utente corrente, passa null
        }
    }

    /**
     * Carica un'immagine del profilo utente nello storage Firebase e aggiorna il documento utente con l'URL dell'immagine.
     *
     * @param userId L'ID dell'utente.
     * @param imageUri L'URI dell'immagine da caricare.
     * @param successCallback Callback chiamata in caso di successo.
     * @param errorCallback Callback chiamata in caso di errore.
     */
    fun uploadProfileImage(userId: String, imageUri: Uri, successCallback: () -> Unit, errorCallback: (Exception) -> Unit) {
        val storageRef = FirebaseStorage.getInstance().reference.child("profile_images/$userId.jpg")

        val uploadTask = storageRef.putFile(imageUri)

        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            storageRef.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                val imageUrl = downloadUri.toString()

                // Ora puoi aggiornare il documento utente con l'URL dell'immagine
                updateUserProfileImage(userId, imageUrl, successCallback, errorCallback)
            } else {
                errorCallback.invoke(task.exception!!)
            }
        }
    }

}