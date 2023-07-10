package de.htw_berlin.quiz_app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController

class CategorySelectionActivity:AppCompatActivity() {
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_selection)

        val subNavHostFragment:NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.subFragment) as NavHostFragment
        navController=subNavHostFragment.navController

        //setupActionBarWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}
//TODO:Delete this file if the fragment works and a new NavHost get set up (MainActivity NavHost)