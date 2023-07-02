package de.htw_berlin.quiz_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView

class Modusauswahl : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modusauswahl)
        val tv_willkommen=findViewById<TextView>(R.id.willkommen)
        val username= intent.getStringExtra("username")

        tv_willkommen.text="Hey $username"
    }
    fun navigateToCategoryActivity(view: View) {
        val intent = Intent(this, CategorySelectionActivity::class.java)
        startActivity(intent)
    }
}