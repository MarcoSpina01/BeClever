package com.example.beclever.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.beclever.databinding.FragmentFilteredLessonsBinding
import com.example.beclever.ui.notifications.NotificationsViewModel
import com.example.beclever.ui.plus.LessonModel
import com.example.beclever.ui.plus.LessonViewModel


class FilteredLessonFragment : Fragment(), FilteredLessonsAdapter.LessonClickListener {

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
        val adapter = FilteredLessonsAdapter(lessons, lessonViewModel, this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onLessonBooked(lesson: LessonModel) {
        // Qui puoi chiamare la funzione per creare la notifica nel ViewModel
        val notificationViewModel = ViewModelProvider(this)[NotificationsViewModel::class.java]
        val message = "La tua lezione di ${lesson.subject} del ${lesson.date} è stata prenotata"
        lesson.userId?.let {
            notificationViewModel.createNotification(message,lesson.userId, lesson.clientId, lesson.lessonId) { success ->
                if (success) {
                    // Aggiorna la UI o gestisci il successo
                } else {
                    // Gestisci l'errore nella creazione della notifica
                }
            }
        }
    }
}