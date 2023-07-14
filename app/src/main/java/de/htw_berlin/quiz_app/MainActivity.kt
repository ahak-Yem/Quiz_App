package de.htw_berlin.quiz_app

import Anmeldung
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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
}
