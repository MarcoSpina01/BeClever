package com.example.beclever.ui.plus


import java.io.Serializable

data class LessonModel(

    val subject: String,
    val date: String,
    val target: String,
    val location: String,
    val cost: String,
    val userId: String?,
    var isBooked: Boolean,
    val lessonId: String,
    val clientId: String

)
    : Serializable
{
    constructor() : this("", "", "", "", "", "",false, "", "")
}