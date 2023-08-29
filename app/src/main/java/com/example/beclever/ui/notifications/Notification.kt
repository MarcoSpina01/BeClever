package com.example.beclever.ui.notifications

import java.io.Serializable
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

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

    fun getTempoPassato(): String {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.BASIC_ISO_DATE
        val currentDate = current.format(formatter).toLong()

        val formatter2 = DateTimeFormatter.ISO_LOCAL_TIME
        val currentTime = current.format(formatter2).toString().replace(":", "").substringBefore(".").toLong()

        return when {
            date != null -> {
                val daysPassed = (currentDate - date!!).toInt()
                val hoursPassed = (currentTime / 10000 - time!! / 10000).toInt()
                val minutesPassed = (currentTime / 100 - time!! / 100).toInt()
                val secondsPassed = (currentTime - time!!).toInt()

                when {
                    daysPassed == 0 -> when {
                        currentTime / 10000 != time!! / 10000 && minutesPassed < 100 -> "${minutesPassed - 40} minuti fa"
                        hoursPassed >= 1 -> "$hoursPassed ore fa"
                        minutesPassed in 1..59 -> "$minutesPassed minuti fa"
                        secondsPassed <= 60 -> "$secondsPassed secondi fa"
                        else -> ""
                    }
                    daysPassed > 30 -> "${currentDate / 10000 - date!! / 10000} anni fa"
                    daysPassed in 30..365 -> "${currentDate / 100 - date!! / 100} mesi fa"
                    else -> "${currentDate - date!!} giorni fa"
                }
            }
            else -> ""
        }
    }
}