package com.example.beclever.ui.profile

/**
 * Classe enum che permette di collegare l'errore generato
 * in caso di cambio password errato ai messaggi
 * da mostrare all'utente nella UI.
 */
enum class ChangePasswordResult {
    SUCCESS,
    CURRENT_PASSWORD_INCORRECT,
    NEW_PASSWORD_TOO_SHORT,
    NEW_PASSWORD_DIFFERENT,
    ERRORE
}
