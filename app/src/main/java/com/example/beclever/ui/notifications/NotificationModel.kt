package com.example.beclever.ui.notifications

import java.io.Serializable
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Modello di dati per rappresentare una notifica.
 *
 * @property message Il testo della notifica.
 * @property date La data della notifica in formato numerico lungo (ad esempio, 20230907 per il 7 settembre 2023).
 * @property time L'orario della notifica in formato numerico lungo (ad esempio, 154532 per le 15:45:32).
 * @property userId L'ID dell'utente a cui è destinata la notifica.
 * @property lessonId L'ID della lezione associata alla notifica.
 * @property clientId L'ID del cliente associato alla notifica.
 */
data class NotificationModel(

    var message: String,
    var date: Long?,
    var time: Long?,
    var userId: String?,
    var lessonId: String?,
    var clientId: String?

) : Serializable {
    /**
     * Costruttore primario con parametri opzionali inizializzati a valori predefiniti vuoti.
     */
    constructor() : this("", null, null, "", "", "")

    /**
     * Calcola il tempo trascorso tra la notifica e il momento attuale in una forma leggibile.
     *
     * @return Una stringa che rappresenta il tempo trascorso dalla notifica (ad esempio, "2 ore fa").
     */
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

            } else {
                if ((currentDate/10000 - date!!/10000).toInt() == 0) {
                    if((currentDate/100 - date!!/100).toInt() == 0) {
                        return "${(currentDate - date!!)} giorni fa"
                    } else {
                        return "${(currentDate/100 - date!!/100)} mesi fa"
                    }
                } else {
                    return "${(currentDate/10000 - date!!/10000)} anni fa"
                }
            }
        }
        return ""
    }
}