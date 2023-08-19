package com.example.beclever.ui.profile

data class User(

    private var nome: String,
    private var email: String

) {

    fun getNome(): String {
        return nome
    }

    fun getEmail(): String {
        return email
    }
}
