package com.example.beclever.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.beclever.databinding.FragmentModifyProfileBinding

/**
 * Fragment per la modifica del profilo utente.
 *
 * Questo fragment consente all'utente di modificare le informazioni del proprio profilo, come nome,
 * cognome, email, bio e qualifica. Utilizza il ViewModel `UserViewModel` per gestire l'aggiornamento
 * delle informazioni del profilo.
 */
class ModifyProfileFragment : Fragment() {

    // Variabile per il binding dei dati
    private var _binding: FragmentModifyProfileBinding? = null
    private val bindingView get() = _binding!!

    // Variabili per l'ID dell'utente e il ViewModel dell'utente
    private lateinit var userId: String
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Ottiene l'ID dell'utente dai dati passati come argomento
        userId = requireArguments().getString("userId", "")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentModifyProfileBinding.inflate(inflater, container, false)
        val root: View = bindingView.root

        // Inizializza il ViewModel dell'utente
        userViewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]

        // Collega il ViewModel al binding per consentire il binding dei dati
        bindingView.viewModel = userViewModel
        bindingView.lifecycleOwner = viewLifecycleOwner // Importante per osservare i LiveData

        // Gestisce il clic sul pulsante di conferma per la modifica del profilo
        bindingView.button7.setOnClickListener {
            // Ottiene le nuove informazioni dal modulo di modifica del profilo
            val newName = bindingView.textInputEditText1.text.toString()
            val newSurname = bindingView.textInputEditText2.text.toString()
            val newEmail = bindingView.textInputEditText3.text.toString()
            val newBio = bindingView.textInputEditText5.text.toString()
            val newQualification = bindingView.textInputEditText6.text.toString()

            // Effettua la modifica del profilo tramite il ViewModel
            userViewModel.updateUserProfile(userId, newName, newSurname, newEmail, newBio, newQualification, requireContext())

            // Torna indietro alla schermata del profilo rimuovendo il fragment corrente
            parentFragmentManager.popBackStack()
        }

        // Gestisce il clic sul pulsante per annullare la modifica del profilo
        bindingView.button8.setOnClickListener {
            // Torna indietro alla schermata del profilo rimuovendo il fragment corrente
            parentFragmentManager.popBackStack()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Libera il binding dei dati quando il fragment viene distrutto
        _binding = null
    }
}
