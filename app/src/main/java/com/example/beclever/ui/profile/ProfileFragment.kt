package com.example.beclever.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.beclever.R
import com.example.beclever.databinding.FragmentProfilenewBinding
import com.example.beclever.ui.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth


class ProfileFragment : Fragment() {

    private var _binding: FragmentProfilenewBinding? = null
    private val bindingView get() = _binding!!

    private lateinit var userViewModel: UserViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProfilenewBinding.inflate(inflater, container, false)
        return bindingView.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userViewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]
        bindingView.viewModel = userViewModel
        bindingView.lifecycleOwner = viewLifecycleOwner

        bindingView.ModifyProfileButton.setOnClickListener {
            val currentUser = FirebaseAuth.getInstance().currentUser
            val userId = currentUser?.uid

            if (userId != null) {
                val fragment = ModifyProfileFragment()
                val bundle = Bundle()
                bundle.putString("userId", userId)
                fragment.arguments = bundle

                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit()

            }
        }

        bindingView.button3.setOnClickListener {

            val fragment = ModifyPasswordFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }

        bindingView.button5.setOnClickListener {
            logout()
        }

        // Aggiungi qui le osservazioni dei LiveData nel ViewModel, se necessario
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



