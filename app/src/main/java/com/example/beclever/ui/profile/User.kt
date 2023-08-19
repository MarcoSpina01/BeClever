package com.example.beclever.ui.profile

data class User(

    private var nome: String,
    private var email: String,
    private var cognome: String,
    private var bio: String,
    private var qualifica: String,


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
}
