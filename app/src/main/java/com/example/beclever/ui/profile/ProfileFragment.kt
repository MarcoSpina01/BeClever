package com.example.beclever.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.beclever.databinding.FragmentProfileBinding
import com.example.beclever.databinding.FragmentProfilenewBinding
import com.example.beclever.ui.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth


class ProfileFragment : Fragment() {

    //private var _binding: FragmentProfileBinding? = null
    private var _binding: FragmentProfilenewBinding? = null
    private val binding get() = _binding!!

    private lateinit var userProfileViewModel: UserProfileViewModel
    private var isDataLoaded = false



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userProfileViewModel = ViewModelProvider(this).get(UserProfileViewModel::class.java)

        // Richiedi il recupero dei dati dell'utente solo se non è già stato caricato
        if (!isDataLoaded) {
            userProfileViewModel.fetchUserData()
            isDataLoaded = true
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfilenewBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Osserva i dati dell'utente nel ViewModel e aggiorna l'interfaccia utente
        /*
        userProfileViewModel.userName.observe(viewLifecycleOwner) { userName ->
            binding.textView4.text = userName
            binding.progressBar.visibility = View.GONE
        }

        binding.ModifyButton.setOnClickListener {
            val intent = Intent(requireContext(), ModifyProfileActivity::class.java)
            startActivity(intent)
        }*/

        binding.button5.setOnClickListener {
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

