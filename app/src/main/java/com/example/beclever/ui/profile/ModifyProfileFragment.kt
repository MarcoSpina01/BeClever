package com.example.beclever.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.beclever.R
import com.example.beclever.databinding.FragmentModifyProfileBinding

class ModifyProfileFragment : Fragment() {


    private var _binding: FragmentModifyProfileBinding? = null
    private val bindingView get() = _binding!!

    private lateinit var userViewModel: UserViewModel

    private lateinit var userId: String // Ottieni l'ID dell'utente loggato da qualche fonte

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Ottieni l'ID dell'utente dagli argomenti
        userId = requireArguments().getString("userId", "")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        lateinit var viewModel: ModifyProfileViewModel
        viewModel = ViewModelProvider(this).get(ModifyProfileViewModel::class.java)
        /*val root = inflater.inflate(R.layout.fragment_modify_profile, container, false)*/

        _binding = FragmentModifyProfileBinding.inflate(inflater, container, false)
        val root: View = bindingView.root

        val nameEditText = root.findViewById<EditText>(R.id.textInputEditText1)
        val emailEditText = root.findViewById<EditText>(R.id.textInputEditText3)
        val saveButton = root.findViewById<Button>(R.id.button7)

        // Recupera i dati del profilo e popola i campi
        viewModel.getUserProfile(userId).observe(viewLifecycleOwner, { userProfile ->
            if (userProfile != null) {
                nameEditText.setText(userProfile.nome)
            }
            if (userProfile != null) {
                emailEditText.setText(userProfile.email)
            }
        })

        saveButton.setOnClickListener {
            val newName = nameEditText.text.toString()
            val newEmail = emailEditText.text.toString()

            // Effettua la modifica del profilo tramite il ViewModel
            viewModel.updateUserProfile(userId, newName, newEmail, context)
        }

        return root
    }
}