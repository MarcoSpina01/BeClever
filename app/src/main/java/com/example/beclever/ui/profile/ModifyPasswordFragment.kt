package com.example.beclever.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.beclever.databinding.FragmentModifyPasswordBinding

class ModifyPasswordFragment : Fragment() {


    private var _binding: FragmentModifyPasswordBinding? = null
    private val bindingView get() = _binding!!
    private lateinit var userViewModel: UserViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentModifyPasswordBinding.inflate(inflater, container, false)

        bindingView.button8.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        bindingView.button7.setOnClickListener {
            if(checkField()) {
                userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
                val newPassword = bindingView.textInputEditText2.text.toString()
                val oldPassword = bindingView.textInputEditText1.text.toString()
                userViewModel.updatePassword(oldPassword, newPassword) { result ->
                    when (result) {
                        ChangePasswordResult.NEW_PASSWORD_DIFFERENT -> {
                            Toast.makeText(requireContext(), "La nuova password deve essere diversa da quella precedente", Toast.LENGTH_SHORT).show()
                        }
                        ChangePasswordResult.NEW_PASSWORD_TOO_SHORT -> {
                            Toast.makeText(requireContext(), "La nuova password deve essere lunga almeno 8 caratteri", Toast.LENGTH_SHORT).show()
                        }
                        ChangePasswordResult.SUCCESS -> {
                            Toast.makeText(requireContext(), "Password cambiata", Toast.LENGTH_SHORT).show()
                            parentFragmentManager.popBackStack()
                        }
                        ChangePasswordResult.CURRENT_PASSWORD_INCORRECT -> {
                            Toast.makeText(requireContext(), "La password attuale inserita non è corretta", Toast.LENGTH_SHORT).show()
                        }
                        ChangePasswordResult.ERRORE -> {
                            Toast.makeText(requireContext(), "Errore", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Per favore riempi tutti i campi", Toast.LENGTH_SHORT).show()
            }
        }
        return bindingView.root

    }

    private fun checkField(): Boolean {
        return (bindingView.textInputEditText1.text?.isNotEmpty() == true &&
            bindingView.textInputEditText2.text?.isNotEmpty() == true &&
            bindingView.textInputEditText3.text?.isNotEmpty() == true)
    }

}