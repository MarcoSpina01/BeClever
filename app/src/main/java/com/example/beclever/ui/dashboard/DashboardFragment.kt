package com.example.beclever.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        dashboardViewModel = ViewModelProvider(this)[DashboardViewModel::class.java]
        bindingView.viewModel = dashboardViewModel
        bindingView.lifecycleOwner = viewLifecycleOwner

        return bindingView.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // Inizializza gli adapter per le lezioni prenotate e pubblicate
        bookedLessonsAdapter = BookedLessonsAdapter(emptyList(), dashboardViewModel)
        publishedLessonsAdapter = PublishedLessonsAdapter(emptyList(), dashboardViewModel)

        // Imposta gli adapter iniziali per le RecyclerView
        bindingView.recyclerViewBookedLessons.adapter = bookedLessonsAdapter
        bindingView.recyclerViewPublishedLessons.adapter = publishedLessonsAdapter

        // Imposta il layout manager per le RecyclerView
        bindingView.recyclerViewBookedLessons.layoutManager = LinearLayoutManager(requireContext())
        bindingView.recyclerViewPublishedLessons.layoutManager = LinearLayoutManager(requireContext())

        showBookedLesson()

        // Imposta i click listener per i pulsanti di filtro
        bindingView.BookedLessons.setOnClickListener {
            showBookedLesson()
        }

        bindingView.PublishedLessons.setOnClickListener {
            showPublishedLesson()
        }

        dashboardViewModel.bookedLessonsList.observe(viewLifecycleOwner) { lessons ->
            bookedLessonsAdapter.updateData(lessons)
        }

        dashboardViewModel.publishedLessonsList.observe(viewLifecycleOwner) { lessons ->
            publishedLessonsAdapter.updateData(lessons)
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

    private fun showBookedLesson() {
        bindingView.recyclerViewBookedLessons.visibility = View.VISIBLE
        bindingView.recyclerViewPublishedLessons.visibility = View.GONE
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid
        userId?.let {
            dashboardViewModel.loadBookedLessons(it)
        }
        moveIndicatorBarToView(bindingView.BookedLessons)
    }

    private fun showPublishedLesson() {
        bindingView.recyclerViewBookedLessons.visibility = View.GONE
        bindingView.recyclerViewPublishedLessons.visibility = View.VISIBLE
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid
        userId?.let {
            dashboardViewModel.loadPublishedLessons(it)
        }
        moveIndicatorBarToView(bindingView.PublishedLessons)
    }
}