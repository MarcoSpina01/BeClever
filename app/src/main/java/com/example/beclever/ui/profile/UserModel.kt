package com.example.beclever.ui.profile

data class UserModel(

    private var nome: String,
    private var email: String,
    private var cognome: String,
    private var bio: String,
    private var qualifica: String,
    private var notificationId: String,
    private var password: String,


    ) {

    fun getNome(): String {
        return nome
    }

    fun getEmail(): String {
        return email
    }

    fun getCognome(): String {
        return cognome
    }

    fun getBio(): String {
        return bio
    }

    fun getQualifica(): String {
        return qualifica
    }

    fun getPassword(): String {
        return password
    }

    val isNomeValid: Boolean
    get() = nome.isNotEmpty()

    val isCognomeValid: Boolean
    get() = cognome.isNotEmpty()

    val isEmailValid: Boolean
    get() = email.isNotEmpty()

    val isPasswordValid: Boolean
        get() = password.isNotEmpty()

}
