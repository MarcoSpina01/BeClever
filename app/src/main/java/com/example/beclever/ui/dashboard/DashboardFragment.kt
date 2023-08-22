package com.example.beclever.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil.setContentView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.beclever.R
import com.example.beclever.databinding.FragmentDashboardBinding
import com.example.beclever.ui.plus.Lesson

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var indicatorBar: View

    private lateinit var layoutBookedLessons: LinearLayout
    private lateinit var layoutPublishedLessons: LinearLayout
    private lateinit var recyclerViewBookedLessons: RecyclerView
    private lateinit var recyclerViewPublishedLessons: RecyclerView
    private lateinit var bookedLessonsData: List<Lesson> // I tuoi dati delle lezioni prenotate
    private lateinit var publishedLessonsData: List<Lesson> // I tuoi dati delle lezioni pubblicate

    val publishedLessons = mutableListOf<Lesson>()
    val bookedLessons = mutableListOf<Lesson>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        recyclerViewBookedLessons = view.findViewById(R.id.recyclerViewBookedLessons)
        recyclerViewPublishedLessons = view.findViewById(R.id.recyclerViewPublishedLessons)

        // Configura le RecyclerView come desiderato
        setupRecyclerView(recyclerViewBookedLessons, /* passi i dati delle lezioni prenotate */)
        setupRecyclerView(recyclerViewPublishedLessons, /* passi i dati delle lezioni pubblicate */)

        layoutBookedLessons = view.findViewById(R.id.layoutMessages)
        layoutPublishedLessons = view.findViewById(R.id.layoutNotifications)

        // Imposta il layout delle lezioni prenotate come visibile all'inizio
        layoutBookedLessons.visibility = View.VISIBLE
        layoutPublishedLessons.visibility = View.GONE

        val bookedLessonsTextView: TextView = view.findViewById(R.id.BookedLessons)
        val publishedLessonsTextView: TextView = view.findViewById(R.id.PublishedLessons)

        indicatorBar = view.findViewById(R.id.lineView)

        // Aggiungi un listener al pulsante "Lezioni prenotate"
        bookedLessonsTextView.setOnClickListener {
            layoutBookedLessons.visibility = View.VISIBLE
            layoutPublishedLessons.visibility = View.GONE
            moveIndicatorBarToView(bookedLessonsTextView)
        }

        // Aggiungi un listener al pulsante "Lezioni pubblicate"
        publishedLessonsTextView.setOnClickListener {
            layoutBookedLessons.visibility = View.GONE
            layoutPublishedLessons.visibility = View.VISIBLE
            moveIndicatorBarToView(publishedLessonsTextView)
        }

        return view
    }

    private fun moveIndicatorBarToView(targetView: View) {
        val targetX = targetView.x
        indicatorBar.animate()
            .x(targetX)
            .setDuration(300)
            .start()
    }


    private fun setupRecyclerView(recyclerView: RecyclerView, lessonData: List<Lesson>) {
        val layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager
        val adapter = DashboardAdapter(lessonData) // Crea l'adapter e passa i dati delle lezioni
        recyclerView.adapter = adapter
    }
    override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }
}