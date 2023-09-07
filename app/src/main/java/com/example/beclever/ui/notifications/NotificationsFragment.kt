package com.example.beclever.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.beclever.databinding.FragmentNotificationsBinding

class NotificationsFragment : Fragment() {

    private lateinit var notificationViewModel: NotificationsViewModel
    private lateinit var notificationsAdapter: NotificationsAdapter
    private var _binding: FragmentNotificationsBinding? = null

    private val bindingView get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = bindingView.root

        notificationViewModel = ViewModelProvider(this)[NotificationsViewModel::class.java]
        bindingView.viewModel = notificationViewModel
        bindingView.lifecycleOwner = viewLifecycleOwner

        val recyclerView: RecyclerView = bindingView.recyclerViewNotifications

        notificationViewModel.notificationsLiveData.observe(viewLifecycleOwner) { notifications ->
            // Aggiorna l'adapter con le nuove notifiche
            notificationsAdapter.updateData(notifications)
        }

        val notificationModels = arguments?.getSerializable("NotificationsList") as? List<NotificationModel>

        notificationsAdapter = NotificationsAdapter(notificationModels ?: emptyList())
        recyclerView.adapter = notificationsAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        notificationViewModel = ViewModelProvider(this)[NotificationsViewModel::class.java]
        bindingView.viewModel = notificationViewModel
        bindingView.lifecycleOwner = viewLifecycleOwner

        val recyclerView: RecyclerView = bindingView.recyclerViewNotifications

        val notificationModels = arguments?.getSerializable("NotificationsList") as? List<NotificationModel>
        if (notificationModels != null) {
            // Inizializza l'adapter qui
            notificationsAdapter = NotificationsAdapter(notificationModels)
            recyclerView.adapter = notificationsAdapter
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
        } else {
            // La conversione non è riuscita o l'argomento non è presente
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}