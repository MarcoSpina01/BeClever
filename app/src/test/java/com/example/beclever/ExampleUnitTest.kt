package com.example.beclever
import com.example.beclever.ui.notifications.NotificationModel
import com.example.beclever.ui.notifications.NotificationsViewModel
import com.example.beclever.ui.profile.ChangePasswordResult
import com.example.beclever.ui.profile.UserViewModel
import org.junit.Test
import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun testUpdatePassword() {

        val passwordService = UserViewModel()

        // Caso 1: Nuova password uguale alla vecchia password
        passwordService.updatePassword("password123", "password123") { result ->
            assertEquals(ChangePasswordResult.NEW_PASSWORD_DIFFERENT, result)
        }

        // Caso 2: Nuova password troppo corta
        passwordService.updatePassword("password123", "short") { result ->
            assertEquals(ChangePasswordResult.NEW_PASSWORD_TOO_SHORT, result)
        }

    }

    @Test
    fun testGetTempoPassato_DaysAgo() {

        val notification = NotificationModel(
            "Messaggio di test",
            20220901,
            12000000,
            "userId",
            "lessonId",
            "clientId"
        )

        val tempoPassato = notification.getTempoPassato()

        assertEquals("1 anni fa", tempoPassato)
    }

    @Test
    fun testGetTempoPassato_MonthAgo() {
        val notification = NotificationModel(
            "Messaggio di test",
            20230101,
            10000000,
            "userId",
            "lessonId",
            "clientId"
        )

        val tempoPassato = notification.getTempoPassato()

        assertEquals("8 mesi fa", tempoPassato)
    }

    @Test
    fun testGetTempoPassato_NullDate() {
        val notification = NotificationModel(
            "Messaggio di test",
            null,
            12000000,
            "userId",
            "lessonId",
            "clientId"
        )

        val tempoPassato = notification.getTempoPassato()

        assertEquals("", tempoPassato)
    }


}