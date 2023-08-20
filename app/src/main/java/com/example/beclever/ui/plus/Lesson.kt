package com.example.beclever.ui.plus

data class Lesson(

    val subject: String,
    val date: String,
    val target: String,
    val location: String,
    val cost: String,
    val userId: String?

)
{
    constructor() : this("", "", "", "", "", null)
}