import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class AuthRepository {

    // Inizializza FirebaseAuth
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    // Funzione per il login
    suspend fun login(username: String, password: String): Boolean {
        return try {
            // Tenta il login usando le credenziali fornite
            val result = auth.signInWithEmailAndPassword(username, password).await()

            // Controlla se il login è riuscito (user != null)
            result.user != null
        } catch (e: Exception) {
            // In caso di errore (ad esempio, credenziali errate), restituisci false
            false
        }
    }

    // Funzione per verificare se l'utente è loggato
    suspend fun isLoggedIn(): Boolean {
        // Ottiene l'utente corrente da FirebaseAuth
        val currentUser = auth.currentUser

        // Restituisce true se l'utente è loggato, altrimenti false
        return currentUser != null
    }
}


