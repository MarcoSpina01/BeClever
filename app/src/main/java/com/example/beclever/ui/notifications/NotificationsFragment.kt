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

/**
 * Fragment per la visualizzazione delle notifiche dell'utente.
 */
class NotificationsFragment : Fragment() {

    // ViewModel per la gestione delle notifiche
    private lateinit var notificationViewModel: NotificationsViewModel

    // Adapter per la visualizzazione delle notifiche
    private lateinit var notificationsAdapter: NotificationsAdapter

    // Binding per l'uso di View Binding
    private var _binding: FragmentNotificationsBinding? = null

    // Proprietà di accesso per il binding
    private val bindingView get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = bindingView.root

        // Inizializza il ViewModel per le notifiche
        notificationViewModel = ViewModelProvider(this)[NotificationsViewModel::class.java]

        // Collega il ViewModel al layout tramite View Binding
        bindingView.viewModel = notificationViewModel
        bindingView.lifecycleOwner = viewLifecycleOwner

        // Inizializza il RecyclerView per visualizzare le notifiche
        val recyclerView: RecyclerView = bindingView.recyclerViewNotifications

        // Osserva le modifiche nella lista delle notifiche nel ViewModel
        notificationViewModel.notificationsLiveData.observe(viewLifecycleOwner) { notifications ->
            // Aggiorna l'adapter con le nuove notifiche
            notificationsAdapter.updateData(notifications)
        }

        // Ottieni le notifiche passate come argomento al fragment (se presenti)
        val notificationModels = arguments?.getSerializable("NotificationsList") as? List<NotificationModel>

        // Inizializza l'adapter con le notifiche ottenute dagli argomenti o una lista vuota se non ci sono argomenti
        notificationsAdapter = NotificationsAdapter(notificationModels ?: emptyList())
        recyclerView.adapter = notificationsAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inizializza nuovamente il ViewModel (non è necessario, duplicato)
        notificationViewModel = ViewModelProvider(this)[NotificationsViewModel::class.java]
        bindingView.viewModel = notificationViewModel
        bindingView.lifecycleOwner = viewLifecycleOwner

        // Ottieni il RecyclerView per visualizzare le notifiche
        val recyclerView: RecyclerView = bindingView.recyclerViewNotifications

        // Ottieni le notifiche passate come argomento al fragment (se presenti)
        val notificationModels = arguments?.getSerializable("NotificationsList") as? List<NotificationModel>
        if (notificationModels != null) {
            // Inizializza l'adapter qui con le notifiche ottenute dagli argomenti
            notificationsAdapter = NotificationsAdapter(notificationModels)
            recyclerView.adapter = notificationsAdapter
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
        } else {
            // Gestisci il caso in cui la conversione non è riuscita o l'argomento non è presente
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}