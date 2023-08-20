package com.example.beclever.ui.plus

import java.io.Serializable

data class Lesson(

    val subject: String,
    val date: String,
    val target: String,
    val location: String,
    val cost: String,
    val userId: String?

)
    : Serializable
{
    constructor() : this("", "", "", "", "", null)
}