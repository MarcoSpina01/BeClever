package com.example.beclever.ui.dashboard

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.beclever.R
import com.example.beclever.databinding.FragmentDashboardBinding


class DashboardFragment : Fragment(R.layout.fragment_dashboard) {

    private lateinit var viewModel: DashboardViewModel
    private lateinit var binding: FragmentDashboardBinding

    private lateinit var indicatorBar: View

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDashboardBinding.bind(view)

        viewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)

        val bookedLessonsAdapter = BookedLessonsAdapter(emptyList())
        val publishedLessonsAdapter = PublishedLessonsAdapter(emptyList())

        binding.recyclerViewBookedLessons.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = bookedLessonsAdapter
        }

        binding.recyclerViewPublishedLessons.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = publishedLessonsAdapter
        }

        viewModel.bookedLessonsLiveData.observe(viewLifecycleOwner) { lessons ->
            bookedLessonsAdapter.submitList(lessons)
            binding.BookedLessons.text = "Lezioni prenotate (${lessons.size})"
        }

        viewModel.publishedLessonsLiveData.observe(viewLifecycleOwner) { lessons ->
            publishedLessonsAdapter.submitList(lessons)
            binding.PublishedLessons.text = "Lezioni pubblicate (${lessons.size})"
        }

        indicatorBar = view.findViewById(R.id.lineView)

        val bookedLessonsTextView: TextView = view.findViewById(R.id.BookedLessons)
        val publishedLessonsTextView: TextView = view.findViewById(R.id.PublishedLessons)
        val layoutBooked: LinearLayout = view.findViewById(R.id.layoutBookedLessons)
        val layoutPublished: LinearLayout = view.findViewById(R.id.layoutPublishedLessons)

        bookedLessonsTextView.setOnClickListener {
            layoutBooked.visibility = View.VISIBLE
            layoutPublished.visibility = View.GONE
            moveIndicatorBarToView(bookedLessonsTextView)
        }

        publishedLessonsTextView.setOnClickListener {
            layoutBooked.visibility = View.GONE
            layoutPublished.visibility = View.VISIBLE
            moveIndicatorBarToView(publishedLessonsTextView)
        }
    }

    private fun moveIndicatorBarToView(targetView: View) {
        val targetX = targetView.x
        indicatorBar.animate()
            .x(targetX)
            .setDuration(300)
            .start()
    }
}




