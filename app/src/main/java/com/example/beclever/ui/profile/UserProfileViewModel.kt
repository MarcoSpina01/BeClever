package com.example.beclever.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class UserProfileViewModel : ViewModel() {

    private val profileRepository = ProfileRepository()

    private val _userName = MutableLiveData<String>()
    private val _eMail = MutableLiveData<String>()

    val userName: LiveData<String>
        get() = _userName

    val eMail: LiveData<String>
        get() = _eMail


    // Metodo per impostare il nome dell'utente
    fun setUserName(name: String) {
        _userName.value = name
    }

    fun setEmail(mail: String) {
        _eMail.value = mail
    }


    // Metodo per ottenere i dati dell'utente da una sorgente (ad es. Firebase, API, ecc.)
    fun fetchUserData() {
        // Effettua il recupero dei dati dell'utente e imposta i valori nel ViewModel
        profileRepository.getData { data ->
            if (data != null) {
                // Fai qualcosa con i dati ricevuti
                val nome = data["first"] as String
                val email = data["email"] as String
                setUserName(nome)
                setEmail(email)
            } else {
            // Nessun dato corrispondente trovato
            } }
    }





}