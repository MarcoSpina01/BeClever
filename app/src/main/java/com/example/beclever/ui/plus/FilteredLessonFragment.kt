package com.example.beclever.ui.plus

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.beclever.R
import com.example.beclever.databinding.FragmentFilteredLessonsBinding
import com.example.beclever.databinding.FragmentModifyProfileBinding
import com.example.beclever.ui.FilteredLessonsAdapter


private var _binding: FragmentFilteredLessonsBinding? = null
private val bindingView get() = _binding!!

class FilteredLessonFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_filtered_lessons, container, false)

        val recyclerView: RecyclerView = root.findViewById(R.id.recyclerViewFilteredLessons)

        val lessons: List<Lesson> = arguments?.getSerializable("filteredLessonsList") as List<Lesson>
        val adapter = FilteredLessonsAdapter(lessons)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true) // Abilita il pulsante di navigazione indietro

        return root
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                // Esegui l'azione desiderata quando l'utente preme il pulsante di navigazione indietro
                requireActivity().onBackPressedDispatcher.onBackPressed()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}