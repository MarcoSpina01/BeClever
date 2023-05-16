package com.example.beclever.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.beclever.R
import com.example.beclever.databinding.FragmentNotificationsBinding

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val messages: TextView = _binding!!.Messages
        val notifications: TextView = _binding!!.Notifications
        val layoutMessages: LinearLayout = _binding!!.layoutMessages
        val layoutNotifications: LinearLayout = _binding!!.layoutNotifications
        val lineView: View = _binding!!.lineView

        messages.setOnClickListener {
            val params = lineView.layoutParams as ConstraintLayout.LayoutParams
            params.startToStart = ConstraintLayout.LayoutParams.PARENT_ID
            params.endToEnd = R.id.guideline
            lineView.layoutParams = params

            layoutMessages.visibility = View.VISIBLE
            layoutNotifications.visibility = View.GONE
        }

        notifications.setOnClickListener {
            val params = lineView.layoutParams as ConstraintLayout.LayoutParams
            params.startToStart = R.id.guideline
            params.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
            lineView.layoutParams = params

            layoutMessages.visibility = View.GONE
            layoutNotifications.visibility = View.VISIBLE
        }

//        val textView: TextView = binding.textNotifications
//        notificationsViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}