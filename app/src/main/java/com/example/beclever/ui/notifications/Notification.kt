package com.example.beclever.ui.notifications

import com.google.type.DateTime

data class Notification(

    private var message: String,
    private var date: DateTime,
    private var userId: String,
    private var clientId: String

) {

}