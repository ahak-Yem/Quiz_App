package de.htw_berlin.quiz_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class Modusauswahl : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modusauswahl)
        val help = findViewById<TextView>(R.id.hilfe)
        val tv_willkommen=findViewById<TextView>(R.id.willkommen)
        val username= intent.getStringExtra("username")
        help.setOnClickListener {

            Toast.makeText(this, "Normalmodus besteht aus Categorie von Fragen. " +
                    "Spielmodus da wird direkt Frage gestelt und falls du eine falsch antwortet vierlierst du das Spiel:)",
                Toast.LENGTH_LONG).show()
        }



        tv_willkommen.text="Hey $username"
    }
    fun navigateToCategoryActivity(view: View) {
        val intent = Intent(this, CategorySelectionActivity::class.java)
        startActivity(intent)
    }
}