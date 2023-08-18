package com.example.beclever

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.beclever.ui.dashboard.DashboardFragment
import com.example.beclever.ui.home.HomeFragment
import com.example.beclever.ui.notifications.NotificationsFragment
import com.example.beclever.ui.plus.PlusFragment
import com.example.beclever.ui.profile.ProfileFragment
import com.example.beclever.ui.profile.UserProfileViewModel

class HomeActivity : AppCompatActivity() {

    private val fragmentManager = supportFragmentManager
    private lateinit var userProfileViewModel: UserProfileViewModel

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                showFragment(HomeFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                showFragment(DashboardFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                showFragment(NotificationsFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_profile -> {
                showFragment(ProfileFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_plus -> {
                showFragment(PlusFragment())
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val navigation = findViewById<BottomNavigationView>(R.id.navigation)
        navigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        if (savedInstanceState == null) {
            showFragment(HomeFragment())
        }
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {

        userProfileViewModel = ViewModelProvider(this)[UserProfileViewModel::class.java]
        userProfileViewModel.fetchUserData()
        return super.onCreateView(name, context, attrs)

    }

    private fun showFragment(fragment: Fragment) {
        fragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}
