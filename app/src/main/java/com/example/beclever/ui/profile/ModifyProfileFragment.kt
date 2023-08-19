package com.example.beclever.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.beclever.databinding.FragmentModifyProfileBinding

class ModifyProfileFragment : Fragment() {

    private var _binding: FragmentModifyProfileBinding? = null
    private val bindingView get() = _binding!!

    private lateinit var userId: String
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userId = requireArguments().getString("userId", "")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentModifyProfileBinding.inflate(inflater, container, false)
        val root: View = bindingView.root

        userViewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]

        bindingView.viewModel = userViewModel // Collega il ViewModel al binding
        bindingView.lifecycleOwner = viewLifecycleOwner // Importante per osservare i LiveData



        bindingView.button7.setOnClickListener {

            val newName = bindingView.textInputEditText1.text.toString()
            val newSurname = bindingView.textInputEditText2.text.toString()
            val newEmail = bindingView.textInputEditText3.text.toString()
            val newBio = bindingView.textInputEditText5.text.toString()
            val newQualification = bindingView.textInputEditText6.text.toString()

            // Effettua la modifica del profilo tramite il ViewModel
            userViewModel.updateUserProfile(userId, newName, newSurname, newEmail, newBio, newQualification, requireContext())
            parentFragmentManager.popBackStack()
        }

        bindingView.button8.setOnClickListener{
            // Torna indietro al Fragment precedente (ProfileFragment)
            parentFragmentManager.popBackStack()
        }

        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
