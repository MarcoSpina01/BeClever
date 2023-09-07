package com.example.beclever.ui.login

import com.google.firebase.auth.FirebaseUser

/**
 * Interfaccia per la gestione delle callback durante il processo di registrazione di un utente.
 */
interface RegistrationCallback {
    /**
     * Callback chiamata quando la registrazione dell'utente è avvenuta con successo.
     *
     * @param user L'oggetto FirebaseUser rappresentante l'utente registrato.
     */
    fun onRegistrationSuccess(user: FirebaseUser?)

    /**
     * Callback chiamata quando si verifica un errore durante la registrazione dell'utente.
     *
     * @param exception L'eccezione che ha causato l'errore durante la registrazione.
     */
    fun onRegistrationFailure(exception: Exception)
}