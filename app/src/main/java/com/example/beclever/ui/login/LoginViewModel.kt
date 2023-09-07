package com.example.beclever.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

/**
 * ViewModel per la gestione dell'accesso e dell'autenticazione degli utenti.
 */
class LoginViewModel : ViewModel() {

    private val _loginSuccess = MutableLiveData<Boolean>()
    val loginSuccess: LiveData<Boolean>
        get() = _loginSuccess

    /**
     * Effettua l'accesso di un utente utilizzando le credenziali fornite.
     *
     * @param username Il nome utente o l'indirizzo email dell'utente.
     * @param password La password dell'utente.
     */
    fun loginUser(username: String, password: String) {
        val auth = FirebaseAuth.getInstance()

        if (username.isEmpty() || password.isEmpty()) {
            // Se il nome utente o la password sono vuoti, l'accesso non è riuscito
            _loginSuccess.value = false
        } else {
            // Effettua il tentativo di accesso con le credenziali fornite
            auth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener { task ->
                    // Imposta il valore di _loginSuccess in base al successo o al fallimento dell'accesso
                    _loginSuccess.value = task.isSuccessful
                }
        }
    }
}

