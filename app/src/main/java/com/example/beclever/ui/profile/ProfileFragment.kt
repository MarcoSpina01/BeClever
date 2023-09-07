package com.example.beclever.ui.profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.beclever.R
import com.example.beclever.databinding.FragmentProfilenewBinding
import com.example.beclever.ui.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage


/**
 * Fragment per la visualizzazione e la modifica del profilo utente.
 *
 * Questo fragment consente all'utente di visualizzare il proprio profilo, inclusa l'immagine del profilo,
 * e di effettuare modifiche al profilo, come l'aggiornamento dell'immagine del profilo e la modifica delle
 * informazioni personali. Utilizza il ViewModel `UserViewModel` per gestire le operazioni relative al profilo
 * dell'utente.
 */
class ProfileFragment : Fragment() {

    // Variabile per il binding dei dati
    private var _binding: FragmentProfilenewBinding? = null
    private val bindingView get() = _binding!!

    // Variabile per il ViewModel dell'utente
    private lateinit var userViewModel: UserViewModel

    // ImageView per l'immagine del profilo
    private lateinit var profileImageView: ImageView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProfilenewBinding.inflate(inflater, container, false)
        return bindingView.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inizializza il ViewModel dell'utente
        userViewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]

        // Collega il ViewModel al binding per consentire il binding dei dati
        bindingView.viewModel = userViewModel
        bindingView.lifecycleOwner = viewLifecycleOwner

        // Inizializza l'ImageView per l'immagine del profilo
        profileImageView = bindingView.imageView2

        // Ottieni l'ID dell'utente corrente da Firebase
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid

        if (userId != null) {
            val userDocumentRef = FirebaseFirestore.getInstance().collection("users").document(userId)

            userDocumentRef.get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        // Ottieni l'URL dell'immagine del profilo dal documento Firestore
                        val imageUrl = documentSnapshot.getString("profileImage")

                        if (!imageUrl.isNullOrEmpty()) {
                            // Carica l'immagine del profilo nell'ImageView utilizzando Glide
                            Glide.with(this)
                                .load(imageUrl)
                                .placeholder(R.drawable.userimage2)
                                .into(profileImageView)
                        }
                    }
                }
        }

        // Gestisce il clic sul pulsante per cambiare l'immagine del profilo
        bindingView.editimageprofile.setOnClickListener {
            // Avvia l'Intent per selezionare un'immagine dalla galleria
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        // Gestisce il clic sul pulsante per modificare il profilo
        bindingView.ModifyProfileButton.setOnClickListener {
            // Ottieni l'ID dell'utente corrente
            val currentUser = FirebaseAuth.getInstance().currentUser
            val userId = currentUser?.uid

            if (userId != null) {
                // Crea un nuovo fragment per la modifica del profilo e passa l'ID dell'utente come argomento
                val fragment = ModifyProfileFragment()
                val bundle = Bundle()
                bundle.putString("userId", userId)
                fragment.arguments = bundle

                // Sostituisci il fragment corrente con il fragment di modifica del profilo
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit()
            }
        }

        // Gestisce il clic sul pulsante per modificare la password
        bindingView.button3.setOnClickListener {
            // Crea un nuovo fragment per la modifica della password e lo sostituisce al fragment corrente
            val fragment = ModifyPasswordFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }

        // Gestisce il clic sul pulsante per visualizzare i contatti
        bindingView.button4.setOnClickListener {
            // Crea un nuovo fragment per la visualizzazione dei contatti e lo sostituisce al fragment corrente
            val fragment = MapsFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }

        // Gestisce il clic sul pulsante di logout
        bindingView.button5.setOnClickListener {
            logout()
        }
    }

    // Gestisce il risultato dell'attività per la selezione dell'immagine
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Ottieni l'URI dell'immagine selezionata
        val selectedImageUri: Uri? = data?.data
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if (selectedImageUri != null) {
                // Mostra l'immagine selezionata nell'ImageView
                profileImageView.setImageURI(selectedImageUri)
            }
        }

        // Ottieni un'istanza di Firebase Storage
        val storage = FirebaseStorage.getInstance()
        // Ottieni un riferimento a un percorso nel tuo storage
        val storageRef = storage.reference.child("profile_images/${userId}.jpg")

        // Carica l'immagine selezionata nel riferimento del tuo storage
        val uploadTask = selectedImageUri?.let { storageRef.putFile(it) }

        // Aggiungi un listener per il completamento del caricamento
        if (uploadTask != null) {
            uploadTask.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // L'immagine è stata caricata con successo
                    // Puoi ottenere l'URL dell'immagine caricata
                    storageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                        val imageUrl = downloadUri.toString()
                        // Ora puoi salvare questo URL nel tuo database Firestore
                        // Aggiorna il documento utente con l'URL dell'immagine del profilo
                        if (userId != null) {
                            updateUserProfileImage(userId, imageUrl)
                        }
                    }
                } else {
                    // Si è verificato un errore durante il caricamento
                }
            }
        }
    }

    // Funzione per aggiornare l'URL dell'immagine del profilo nel database Firestore
    private fun updateUserProfileImage(userId: String, imageUrl: String) {
        val db = FirebaseFirestore.getInstance()

        val userRef = db.collection("users").document(userId)

        val data = hashMapOf(
            "profileImage" to imageUrl
        )

        userRef.update(data as Map<String, Any>)
            .addOnSuccessListener {
                // L'URL dell'immagine è stato aggiunto al documento utente con successo
                Toast.makeText(context, "L'URL dell'immagine è stato aggiunto al documento utente con successo", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                // Si è verificato un errore durante l'aggiornamento del documento utente
                // Gestisci l'errore di conseguenza
                Toast.makeText(context, "Errore durante il caricamento", Toast.LENGTH_SHORT).show()
            }
    }

    // Funzione per effettuare il logout
    private fun logout() {
        FirebaseAuth.getInstance().signOut()
        val activityContext = requireContext()
        val intent = Intent(activityContext, LoginActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    companion object {
        private const val PICK_IMAGE_REQUEST = 123
    }
}



