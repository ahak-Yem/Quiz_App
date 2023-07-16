package de.htw_berlin.quiz_app

import Anmeldung
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View.OnCreateContextMenuListener
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext

class MainActivity : AppCompatActivity() {
    private var isLoggedInn = false
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
        isLoggedInn = sharedPreferences.getBoolean("isLoggedIn", false)

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


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (isLoggedInn) {
            menuInflater.inflate(R.menu.home_menu, menu)
            return true
        }
        return false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId)
        {
            R.id.itemapropos->{




            }




            R.id.itemmodusauswahl ->{
                Toast.makeText(this, "Modusauswahl", Toast.LENGTH_SHORT).show()

            }



            R.id.itemprofil->{

                Toast.makeText(this, "Mein Profil", Toast.LENGTH_SHORT).show()

            }
            R.id.itemrating->{
                Toast.makeText(this, "Rating", Toast.LENGTH_SHORT).show()
            }
        }




        return super.onOptionsItemSelected(item)
    }
}
