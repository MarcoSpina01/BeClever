package com.example.beclever.ui.notifications

import java.io.Serializable

data class Notification(

    var message: String,
    var date: String?,
    var userId: String?,
    var lessonId: String?

) : Serializable
{
    constructor() : this("", null, "", "")
}