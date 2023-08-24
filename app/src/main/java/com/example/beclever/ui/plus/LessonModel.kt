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
    val dateBooking: Long?,
    val timeBooking: Long?,
    val canceled: Boolean,
    val dateCancel: Long?,
    val timeCancel: Long?,


    )
    : Serializable
{
    constructor() : this("", "", "", "", "", "",false,
                         "", "", null, null, false, null, null)
}