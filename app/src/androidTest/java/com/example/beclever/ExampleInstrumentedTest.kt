package com.example.beclever

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.beclever.ui.login.LoginActivity
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
    class LoginActivityTest {

        @get:Rule
        val activityRule = ActivityScenarioRule(LoginActivity::class.java)

        @Test
        fun testLogin() {
            // Inserisci il tuo nome utente e password
            val username = "nome.cognome@email.com"
            val password = "password"

            // Inserisci il nome utente
            Espresso.onView(ViewMatchers.withId(R.id.email))
                .perform(ViewActions.typeText(username), ViewActions.closeSoftKeyboard())

            // Inserisci la password
            Espresso.onView(ViewMatchers.withId(R.id.password))
                .perform(ViewActions.typeText(password), ViewActions.closeSoftKeyboard())

            // Fai clic sul pulsante di accesso
            Espresso.onView(ViewMatchers.withId(R.id.login))
                .perform(ViewActions.click())

            // Ottieni uno scenario dell'attività corrente
            val scenario = activityRule.scenario

            // Verifica lo stato di login
            scenario.onActivity { activity ->
                assertFalse(activity.isUserLoggedIn())
            }
        }
    }