package com.example.beclever.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.beclever.databinding.FragmentProfilenewBinding
import com.example.beclever.ui.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth


class ProfileFragment : Fragment() {

    private var _binding: FragmentProfilenewBinding? = null
    private val bindingView get() = _binding!!

    private lateinit var viewModel: UserProfileViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProfilenewBinding.inflate(inflater, container, false)
        val root: View = bindingView.root

        viewModel = ViewModelProvider(requireActivity()).get(UserProfileViewModel::class.java)
        bindingView.viewModel = viewModel // Collega il ViewModel al binding
        bindingView.lifecycleOwner = viewLifecycleOwner // Importante per osservare i LiveData

        viewModel.fetchUserData()

        bindingView.ModifyProfileButton.setOnClickListener {
            val intent = Intent(requireContext(), ModifyProfileActivity::class.java)
            startActivity(intent)
        }

        bindingView.button5.setOnClickListener {
            logout()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun logout() {
        FirebaseAuth.getInstance().signOut()
        val activityContext = requireContext()
        val intent = Intent(activityContext, LoginActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }
}


