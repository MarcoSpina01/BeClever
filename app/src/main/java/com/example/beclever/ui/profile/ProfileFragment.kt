package com.example.beclever.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.beclever.databinding.FragmentProfileBinding
import com.example.beclever.ui.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth


class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var userProfileViewModel: UserProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userProfileViewModel = ViewModelProvider(this).get(UserProfileViewModel::class.java)

        // Richiedi il recupero dei dati dell'utente
        userProfileViewModel.fetchUserData()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val userProfileViewModel = ViewModelProvider(this).get(UserProfileViewModel::class.java)

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        userProfileViewModel.userName.observe(viewLifecycleOwner) { userName ->
            binding.textView4.text = userName
        }

        binding.ModifyButton.setOnClickListener {
            val intent = Intent(requireContext(), ModifyProfileActivity::class.java)
            startActivity(intent)
        }

        binding.button6.setOnClickListener {
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

