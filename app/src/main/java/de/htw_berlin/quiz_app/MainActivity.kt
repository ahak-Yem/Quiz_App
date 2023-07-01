package de.htw_berlin.quiz_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val start = findViewById<Button>(R.id.Start_btn)
        val username = findViewById<EditText>(R.id.Username)
        val txterror = findViewById<TextView>(R.id.textinput_error)
        start.setOnClickListener {
            txterror.visibility = View.VISIBLE
            val txtusername = username.text.toString()
            if (txtusername.trim().isEmpty()) {
                txterror.text = "Name eingeben"
                txterror.visibility = View.VISIBLE
            } else {
                username.setText("")

                val intent = Intent(this, Modusauswahl::class.java)
                intent.putExtra("username", txtusername)
                startActivity(intent)
            }

        }
    }
}