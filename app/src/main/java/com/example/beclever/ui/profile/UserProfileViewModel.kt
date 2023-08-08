package com.example.beclever.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class UserProfileViewModel : ViewModel() {

    private val profileRepository = ProfileRepository()

    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String>
        get() = _userName

    // Metodo per impostare il nome dell'utente
    fun setUserName(name: String) {
        _userName.value = name
    }

    // Metodo per ottenere i dati dell'utente da una sorgente (ad es. Firebase, API, ecc.)
    fun fetchUserData() {
        // Effettua il recupero dei dati dell'utente e imposta i valori nel ViewModel
        profileRepository.getData { data ->
            if (data != null) {
                // Fai qualcosa con i dati ricevuti
                val nome = data["first"] as String
                setUserName(nome)
            } else {
            // Nessun dato corrispondente trovato
            } }
    }





}