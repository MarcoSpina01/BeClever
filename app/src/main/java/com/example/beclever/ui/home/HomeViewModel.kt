package com.example.beclever.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.beclever.ui.profile.User
import com.example.beclever.ui.profile.UserModel

class HomeViewModel : ViewModel() {

    private val homeModel = HomeModel()

    private val _home = MutableLiveData<HomeModel>()

    val home: LiveData<HomeModel>
        get() = _home


}