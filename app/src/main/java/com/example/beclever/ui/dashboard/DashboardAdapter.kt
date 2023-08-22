package com.example.beclever.ui.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.beclever.R
import com.example.beclever.ui.plus.Lesson

class DashboardAdapter(private val lessonList: List<Lesson>) : RecyclerView.Adapter<LessonAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Definisci le viste all'interno di un elemento dell'elenco (layout dell'elemento)
        // Ad esempio, potresti avere TextViews, ImageViews, ecc.
        // E poi in onBindViewHolder, popoli queste viste con i dati delle lezioni
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Crea una vista per un elemento dell'elenco utilizzando il layout specifico
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_bookedlessons, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val lesson = lessonList[position]

        // Qui popoli le viste all'interno dell'elemento dell'elenco con i dati della lezione
        // Ad esempio, puoi impostare il testo di TextViews o l'immagine di ImageViews
        // utilizzando i dati della lezione dalla posizione "position"
    }

    override fun getItemCount(): Int {
        return lessonList.size
    }
}
