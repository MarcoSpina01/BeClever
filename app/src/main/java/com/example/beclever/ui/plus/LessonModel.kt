package com.example.beclever.ui.plus


import java.io.Serializable

/**
 * Classe dati per rappresentare una lezione.
 *
 * @property subject La materia della lezione.
 * @property date La data della lezione nel formato "dd/MM/yyyy".
 * @property target Il target della lezione.
 * @property location La località della lezione.
 * @property cost Il costo della lezione nel formato "numero €".
 * @property hour L'orario della lezione.
 * @property userId L'ID dell'utente che ha creato la lezione.
 * @property isBooked Indica se la lezione è stata prenotata.
 * @property lessonId L'ID univoco della lezione.
 * @property clientId L'ID del cliente che ha prenotato la lezione.
 * @property dateBooking La data in cui è stata effettuata la prenotazione.
 * @property timeBooking L'orario in cui è stata effettuata la prenotazione.
 * @property canceled Indica se la lezione è stata cancellata.
 * @property dateCancel La data in cui è stata effettuata la cancellazione.
 * @property timeCancel L'orario in cui è stata effettuata la cancellazione.
 */
data class LessonModel(
    val subject: String,
    val date: String,
    val target: String,
    val location: String,
    val cost: String,
    val hour: String,
    val userId: String?,
    var isBooked: Boolean,
    val lessonId: String,
    var clientId: String,
    val dateBooking: Long?,
    val timeBooking: Long?,
    val canceled: Boolean,
    val dateCancel: Long?,
    val timeCancel: Long?,
) : Serializable {
    /**
     * Costruttore secondario per inizializzare una lezione con valori di default.
     */
    constructor() : this(
        "", "", "", "", "", "", "", false,
        "", "", null, null, false, null, null
    )
}