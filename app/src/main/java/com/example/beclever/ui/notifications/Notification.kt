package com.example.beclever.ui.notifications

import java.io.Serializable

data class Notification(

    var message: String,
    var date: Long?,
    var time: Long?,
    var userId: String?,
    var lessonId: String?,
    var clientId: String?

) : Serializable
{
    constructor() : this("", null, null, "", "", "")
}