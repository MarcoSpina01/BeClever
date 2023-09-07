package com.example.beclever

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.beclever.ui.login.LoginActivity
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class LoginActivityTest {

    @Test
    fun testLoginButton() {

        val scenario = ActivityScenario.launch(LoginActivity::class.java)

        Espresso.onView(ViewMatchers.withId(R.id.email)).perform(ViewActions.typeText("nome.cognome@email.com"))
        Espresso.onView(ViewMatchers.withId(R.id.password)).perform(ViewActions.typeText("password"))

        // Fai clic sul pulsante di login
        Espresso.onView(ViewMatchers.withId(R.id.login)).perform(ViewActions.click())


        // Chiudi lo scenario dell'Activity
        scenario.close()
    }

}