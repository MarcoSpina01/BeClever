package com.example.beclever.ui.profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.beclever.R
import com.example.beclever.databinding.FragmentProfilenewBinding
import com.example.beclever.ui.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class ProfileFragment : Fragment() {

    private var _binding: FragmentProfilenewBinding? = null
    private val bindingView get() = _binding!!

    private lateinit var userViewModel: UserViewModel

    private lateinit var profileImageView: ImageView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProfilenewBinding.inflate(inflater, container, false)
        return bindingView.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userViewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]
        bindingView.viewModel = userViewModel
        bindingView.lifecycleOwner = viewLifecycleOwner

        profileImageView = bindingView.imageView2

        bindingView.editimageprofile.setOnClickListener{
                // Avvia l'Intent per selezionare un'immagine dalla galleria
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

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

        bindingView.button.setOnClickListener {

            val fragment = SettingsFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }

        bindingView.button4.setOnClickListener {

            val fragment = MapsFragment()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

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

    fun updateUserProfileImage(userId: String, imageUrl: String) {
        val db = FirebaseFirestore.getInstance()

        val userRef = db.collection("users").document(userId)

        val data = hashMapOf(
            "profileImage" to imageUrl
        )

        userRef.update(data as Map<String, Any>)
            .addOnSuccessListener {
                // L'URL dell'immagine è stato aggiunto al documento utente con successo
            }
            .addOnFailureListener { e ->
                // Si è verificato un errore durante l'aggiornamento del documento utente
                // Gestisci l'errore di conseguenza
            }
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

    companion object {
        private const val PICK_IMAGE_REQUEST = 123
    }
}



