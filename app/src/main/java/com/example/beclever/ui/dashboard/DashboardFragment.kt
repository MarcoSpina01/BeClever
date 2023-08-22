package com.example.beclever.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil.setContentView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.beclever.R
import com.example.beclever.databinding.FragmentDashboardBinding
import com.example.beclever.databinding.FragmentFilteredLessonsBinding
import com.example.beclever.ui.plus.LessonModel
import com.google.firebase.auth.FirebaseAuth

private var _binding: FragmentDashboardBinding? = null
private val bindingView get() = _binding!!

private lateinit var dashboardViewModel: DashboardViewModel
private lateinit var bookedLessonsAdapter: BookedLessonsAdapter
private lateinit var publishedLessonsAdapter: PublishedLessonsAdapter

class DashboardFragment : Fragment() {
    private lateinit var viewModel: DashboardViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = bindingView.root
        // Inizializza la ViewModel
        dashboardViewModel = ViewModelProvider(this)[DashboardViewModel::class.java]
        bindingView.viewModel = dashboardViewModel
        bindingView.lifecycleOwner = viewLifecycleOwner

        bookedLessonsAdapter = BookedLessonsAdapter(emptyList()) // Passa la lista di lezioni prenotate
        publishedLessonsAdapter = PublishedLessonsAdapter(emptyList()) // Passa la lista di lezioni pubblicate


        // Collega gli adapter alle RecyclerView corrispondenti
        bindingView.recyclerViewBookedLessons.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = bookedLessonsAdapter
        }

        bindingView.recyclerViewPublishedLessons.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = publishedLessonsAdapter
        }

        dashboardViewModel.bookedLessonsList.observe(viewLifecycleOwner) { bookedLessons ->
            // Aggiorna l'adapter per lezioni prenotate
            bookedLessonsAdapter.updateData(bookedLessons)
        }

        dashboardViewModel.publishedLessonsList.observe(viewLifecycleOwner) { publishedLessons ->
            // Aggiorna l'adapter per lezioni pubblicate
            publishedLessonsAdapter.updateData(publishedLessons)
        }

        // Carica le lezioni prenotate e pubblicate per l'utente corrente
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid

        if (userId != null) {
            // Ora hai l'ID dell'utente corrente, puoi usarlo nelle tue operazioni
            dashboardViewModel.loadBookedLessons(userId)
            dashboardViewModel.loadPublishedLessons(userId)
        } else {
            // L'utente non è autenticato, fai qualcosa di conseguenza
        }

        // ... Altri codici

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bookedLessonsTextView = bindingView.BookedLessons
        val publishedLessonsTextView = bindingView.PublishedLessons

        // Imposta il click listener per il pulsante delle lezioni prenotate
        bindingView.BookedLessons.setOnClickListener {
            bindingView.recyclerViewBookedLessons.visibility = View.VISIBLE
            bindingView.recyclerViewPublishedLessons.visibility = View.GONE
            moveIndicatorBarToView(bookedLessonsTextView)
        }

        // Imposta il click listener per il pulsante delle lezioni pubblicate
        bindingView.PublishedLessons.setOnClickListener {
            bindingView.recyclerViewBookedLessons.visibility = View.GONE
            bindingView.recyclerViewPublishedLessons.visibility = View.VISIBLE
            moveIndicatorBarToView(publishedLessonsTextView)
        }
    }

    private fun moveIndicatorBarToView(targetView: View) {
        var indicatorBar = bindingView.lineView
        val targetX = targetView.x
        indicatorBar.animate()
            .x(targetX)
            .setDuration(300)
            .start()
    }
}