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
import com.google.firebase.auth.FirebaseAuth

class NotificationsFragment : Fragment() {

    private lateinit var notificationViewModel: NotificationsViewModel
    private lateinit var notificationsAdapter: NotificationsAdapter
    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
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

        val notifications = arguments?.getSerializable("NotificationsList") as? List<Notification>

        notificationsAdapter = NotificationsAdapter(notifications ?: emptyList())
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

        val notifications = arguments?.getSerializable("NotificationsList") as? List<Notification>
        if (notifications != null) {
            // Inizializza l'adapter qui
            notificationsAdapter = NotificationsAdapter(notifications)
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

//    private fun showNotifications() {
//
//        val currentUser = FirebaseAuth.getInstance().currentUser
//        val userId = currentUser?.uid
//        userId?.let {
//            notificationViewModel.(it)
//        }
//    }
}