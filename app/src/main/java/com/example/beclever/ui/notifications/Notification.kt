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

        if (date != null) {
            if ((currentDate - date!!).toInt() == 0) {
                if(currentTime/10000 != time!!/10000 && (currentTime/100 - time!!/100).toInt() < 100) {
                    return "${(currentTime/100 - time!!/100) - 40} minuti fa"
                }
                if((currentTime/10000 - time!!/10000).toInt() >= 1) {
                    return "${(currentTime/10000 - time!!/10000) } ore fa"
                }
                if((currentTime/100 - time!!/100).toInt() in 1..59 ) {
                    return "${(currentTime/100 - time!!/100)} minuti fa"
                }

                if((currentTime - time!!).toInt() <= 60) {
                    return "${(currentTime - time!!)} secondi fa"
                }


                return ""
//
            } else {
                if((currentDate - date!!).toInt() > 30) {
                    return "${(currentDate/10000 - date!!/10000)} anni fa"

                }
                if((currentDate - date!!).toInt() in 30..365) {
                    return "${(currentDate/100 - date!!/100)} mesi fa"
                }
                if((currentDate - date!!).toInt() <= 30) {
                    return "${(currentDate - date!!)} giorni fa"
                }


                return ""

            }
        }
        else return ""
    }
}