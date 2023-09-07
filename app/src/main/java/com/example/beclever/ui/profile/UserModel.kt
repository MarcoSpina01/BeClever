package com.example.beclever.ui.profile

/**
 * Questa classe rappresenta un modello di dati per un utente.
 *
 * @property nome Il nome dell'utente.
 * @property email L'indirizzo email dell'utente.
 * @property cognome Il cognome dell'utente.
 * @property bio Una breve biografia o descrizione dell'utente.
 * @property qualifica La qualifica o titolo dell'utente.
 * @property notificationId L'ID delle notifiche associate all'utente.
 * @property password La password dell'utente.
 */
data class UserModel(
    private var nome: String,
    private var email: String,
    private var cognome: String,
    private var bio: String,
    private var qualifica: String,
    private var notificationId: String,
    private var password: String
) {
    /**
     * Restituisce il nome dell'utente.
     */
    fun getNome(): String {
        return nome
    }

    /**
     * Restituisce l'indirizzo email dell'utente.
     */
    fun getEmail(): String {
        return email
    }

    /**
     * Restituisce il cognome dell'utente.
     */
    fun getCognome(): String {
        return cognome
    }

    /**
     * Restituisce la biografia o descrizione dell'utente.
     */
    fun getBio(): String {
        return bio
    }

    /**
     * Restituisce la qualifica o il titolo dell'utente.
     */
    fun getQualifica(): String {
        return qualifica
    }

    /**
     * Restituisce la password dell'utente.
     */
    fun getPassword(): String {
        return password
    }

    /**
     * Verifica se il nome dell'utente è valido (non vuoto).
     */
    val isNomeValid: Boolean
        get() = nome.isNotEmpty()

    /**
     * Verifica se il cognome dell'utente è valido (non vuoto).
     */
    val isCognomeValid: Boolean
        get() = cognome.isNotEmpty()

    /**
     * Verifica se l'indirizzo email dell'utente è valido (non vuoto).
     */
    val isEmailValid: Boolean
        get() = email.isNotEmpty()

    /**
     * Verifica se la password dell'utente è valida (non vuota).
     */
    val isPasswordValid: Boolean
        get() = password.isNotEmpty()
}
