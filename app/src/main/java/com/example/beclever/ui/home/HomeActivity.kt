package com.example.beclever.ui.home

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.beclever.R
import com.example.beclever.ui.dashboard.DashboardFragment
import com.example.beclever.ui.notifications.NotificationsFragment
import com.example.beclever.ui.plus.PlusFragment
import com.example.beclever.ui.profile.ProfileFragment
import com.example.beclever.ui.profile.UserViewModel

class HomeActivity : AppCompatActivity() {

    private val fragmentManager = supportFragmentManager
    private lateinit var userViewModel: UserViewModel

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                showFragment(HomeFragment(), R.anim.fade_in, R.anim.fade_out)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                showFragment(DashboardFragment(), R.anim.fade_in, R.anim.fade_out)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                showFragment(NotificationsFragment(), R.anim.fade_in, R.anim.fade_out)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_profile -> {
                showFragment(ProfileFragment(), R.anim.fade_in, R.anim.fade_out)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_plus -> {
                showFragment(PlusFragment(), R.anim.fade_in, R.anim.fade_out)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        userViewModel.fetchUserData()

        val navigation = findViewById<BottomNavigationView>(R.id.navigation)
        navigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        if (savedInstanceState == null) {
            showFragment(HomeFragment(), R.anim.fade_in, R.anim.fade_out)
        }
    }


    private fun showFragment(fragment: Fragment, enterAnim: Int, exitAnim: Int) {
        fragmentManager.beginTransaction()
            .setCustomAnimations(enterAnim, exitAnim)
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

}
