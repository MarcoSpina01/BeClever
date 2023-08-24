package com.example.beclever.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.beclever.databinding.FragmentNotificationsBinding

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

        notificationViewModel = ViewModelProvider(this)[NotificationsViewModel::class.java]
        bindingView.viewModel = notificationViewModel
        bindingView.lifecycleOwner = viewLifecycleOwner

        return bindingView.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}