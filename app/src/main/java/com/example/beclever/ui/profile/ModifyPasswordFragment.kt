package com.example.beclever.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.beclever.databinding.FragmentModifyPasswordBinding

/**
 * Fragment per la modifica della password dell'utente.
 *
 * Questo fragment consente all'utente di inserire la sua password attuale e la nuova password,
 * verificando che rispetti i requisiti di lunghezza e che sia diversa da quella attuale.
 * In caso di successo, la password verrà aggiornata.
 */
class ModifyPasswordFragment : Fragment() {

    // Dichiarazione del binding
    private var _binding: FragmentModifyPasswordBinding? = null
    private val bindingView get() = _binding!!

    // Dichiarazione del viewmodel a cui è collegata la schermata
    private lateinit var userViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inizializzazione del binding
        _binding = FragmentModifyPasswordBinding.inflate(inflater, container, false)

        // Gestisce il clic sul pulsante 'annulla' riportando l'utente alla schermata profilo
        bindingView.button8.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        // Gestisce il clic sul pulsante 'salva'
        bindingView.button7.setOnClickListener {
            // Verifica se tutti i campi richiesti sono stati compilati
            if (checkField()) {
                // Inizializza il ViewModel per la gestione dell'utente
                userViewModel = ViewModelProvider(this)[UserViewModel::class.java]

                // Ottiene la nuova password e la vecchia password dall'interfaccia utente
                val newPassword = bindingView.textInputEditText2.text.toString()
                val oldPassword = bindingView.textInputEditText1.text.toString()

                // Chiamata al ViewModel per aggiornare la password
                userViewModel.updatePassword(oldPassword, newPassword) { result ->
                    // Gestisce il risultato dell'aggiornamento della password
                    when (result) {
                        ChangePasswordResult.NEW_PASSWORD_DIFFERENT -> {
                            // Messaggio se la nuova password è uguale a quella precedente
                            Toast.makeText(requireContext(), "La nuova password deve essere diversa da quella precedente", Toast.LENGTH_SHORT).show()
                        }
                        ChangePasswordResult.NEW_PASSWORD_TOO_SHORT -> {
                            // Messaggio se la nuova password è troppo corta
                            Toast.makeText(requireContext(), "La nuova password deve essere lunga almeno 8 caratteri", Toast.LENGTH_SHORT).show()
                        }
                        ChangePasswordResult.SUCCESS -> {
                            // Messaggio se l'aggiornamento della password è riuscito
                            Toast.makeText(requireContext(), "Password cambiata", Toast.LENGTH_SHORT).show()
                            // Torna alla schermata precedente rimuovendo il fragment corrente dalla back stack
                            parentFragmentManager.popBackStack()
                        }
                        ChangePasswordResult.CURRENT_PASSWORD_INCORRECT -> {
                            // Messaggio se la password attuale è errata
                            Toast.makeText(requireContext(), "La password attuale inserita non è corretta", Toast.LENGTH_SHORT).show()
                        }
                        ChangePasswordResult.ERRORE -> {
                            // Messaggio in caso di errore generico
                            Toast.makeText(requireContext(), "Errore", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {
                // Messaggio se non tutti i campi richiesti sono stati compilati
                Toast.makeText(requireContext(), "Per favore riempi tutti i campi", Toast.LENGTH_SHORT).show()
            }
        }

        return bindingView.root

    }

    // Verifica che tutti i campi siano stati compilati
    private fun checkField(): Boolean {
        return (bindingView.textInputEditText1.text?.isNotEmpty() == true &&
                bindingView.textInputEditText2.text?.isNotEmpty() == true &&
                bindingView.textInputEditText3.text?.isNotEmpty() == true)
    }
}