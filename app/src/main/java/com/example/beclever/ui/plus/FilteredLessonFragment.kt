package com.example.beclever.ui.plus

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.beclever.R
import com.example.beclever.databinding.FragmentFilteredLessonsBinding
import com.example.beclever.ui.FilteredLessonsAdapter


class FilteredLessonFragment : Fragment() {

    private var _binding: FragmentFilteredLessonsBinding? = null
    private val bindingView get() = _binding!!

    private lateinit var lessonViewModel: LessonViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilteredLessonsBinding.inflate(inflater, container, false)
        val root: View = bindingView.root

        lessonViewModel = ViewModelProvider(this)[LessonViewModel::class.java]

        // Collega il ViewModel al binding
        bindingView.viewModel = lessonViewModel
        bindingView.lifecycleOwner = viewLifecycleOwner

        val recyclerView: RecyclerView = bindingView.recyclerViewFilteredLessons

        val lessons: List<LessonModel> = arguments?.getSerializable("filteredLessonsList") as List<LessonModel>
        val adapter = FilteredLessonsAdapter(lessons)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}