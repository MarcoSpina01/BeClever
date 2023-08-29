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
                if((currentTime - time!!).toInt() <= 60) {
                    return "${(currentTime - time!!)} secondi fa"
                }
                if((currentTime - time!!).toInt() in 61..3600) {
                    return "${(currentTime - time!!) / 60} minuti fa"
                }
                if((currentTime - time!!).toInt() > 3600) {
                    return "${(currentTime - time!!) / 60 / 60} ore fa"
                }
                return ""
//
            } else {
                if((currentDate - date!!).toInt() <= 30) {
                    return "${(currentDate - date!!)} giorni fa"
                }
                if((currentDate - date!!).toInt() in 30..365) {
                    return "${(currentDate/100 - date!!/100)} mesi fa"
                }
                if((currentDate - date!!).toInt() > 30) {
                    return "${(currentDate/10000 - date!!/10000)} anni fa"
                }
                return ""

            }
        }
        else return ""
    }
}