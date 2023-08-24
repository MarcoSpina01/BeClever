package com.example.beclever.ui.plus


import java.io.Serializable
import java.sql.Time
import java.util.*

data class LessonModel(

    val subject: String,
    val date: String,
    val target: String,
    val location: String,
    val cost: String,
    val userId: String?,
    var isBooked: Boolean,
    val lessonId: String,
    val clientId: String,
    val dateBooking: Date?,
    val timeBooking: Time?,
    val canceled: Boolean,
    val dateCancel: Date?,
    val timeCancel: Time?,


    )
    : Serializable
{
    constructor() : this("", "", "", "", "", "",false,
                         "", "", null, null, false, null, null)
}