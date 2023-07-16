package de.htw_berlin.quiz_app

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {
    lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Setup the bottom navigation view
        setupBottomNavigation()

        if (savedInstanceState == null) {
            loadFragment(Anmeldung())
        }
        val sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)

        if (isLoggedIn) {
            val name = sharedPreferences.getString("name", "") ?: ""
            val fragment = ModusAuswahl()
            val bundle = Bundle()
            bundle.putString("name", name)
            fragment.arguments = bundle
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit()
        } else {
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, Anmeldung()).commit()
        }

    }
    private fun setupBottomNavigation() {
        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.tab1 -> {
                    val sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
                    val name = sharedPreferences.getString("name", "") ?: ""
                    val bundle = Bundle()
                    val fragment = ModusAuswahl() // Replace "ModusFragment" with your actual fragment class for ModusFragment
                    bundle.putString("name", name)
                    fragment.arguments = bundle
                    loadFragment(fragment)
                    return@setOnItemSelectedListener true
                }
                R.id.tab2 -> {
                    loadFragment(SessionsFragment())
                    return@setOnItemSelectedListener true
                }
                R.id.tab3 -> {
                    val sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
                    val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
                    val fragment = Anmeldung()
                    supportFragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    if (isLoggedIn) {
                        // Clear shared preferences to log out the user
                        val editor = sharedPreferences.edit()
                        editor.clear()
                        editor.apply()

                        // Redirect to the Anmeldung fragment
                        loadFragment(fragment)
                    } else {
                        // The user is not logged in, simply navigate to Anmeldung fragment
                        loadFragment(fragment)
                    }
                    return@setOnItemSelectedListener true
                }
                else -> return@setOnItemSelectedListener false
            }
        }
    }
    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
        if (fragment is Anmeldung) {
            bottomNavigationView.visibility = View.GONE
        } else {
            bottomNavigationView.visibility = View.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        val sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)

        if (currentFragment is Anmeldung) {
            // If the current fragment is AnmeldungFragment, hide the bottom navigation
            bottomNavigationView.visibility = View.GONE
        } else {
            // For other fragments, show the bottom navigation
            bottomNavigationView.visibility = View.VISIBLE
        }

        if (isLoggedIn) {
            // User is logged in, check if the current fragment is AnmeldungFragment
            if (currentFragment is Anmeldung) {
                // If the current fragment is AnmeldungFragment, replace it with ModusFragment
                val name = sharedPreferences.getString("name", "") ?: ""
                val fragment = ModusAuswahl() // Replace "ModusFragment" with your actual fragment class for ModusFragment
                val bundle = Bundle()
                bundle.putString("name", name)
                fragment.arguments = bundle
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit()
            }
        }
    }
}
