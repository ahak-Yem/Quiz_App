package de.htw_berlin.quiz_app

import com.google.firebase.firestore.FirebaseFirestore

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.FirebaseApp

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FirebaseApp.initializeApp(this) // Initialize Firebase

        val start = findViewById<Button>(R.id.Start_btn)
        val name = findViewById<EditText>(R.id.Username)
        val spitzname = findViewById<EditText>(R.id.Spitzname)
        val txterror = findViewById<TextView>(R.id.textinput_error)

        start.setOnClickListener {
            txterror.visibility = View.VISIBLE
            val txtusername = name.text.toString()
            val txtspitzname = spitzname.text.toString()
            if (txtusername.trim().isEmpty()) {
                txterror.text = "Name eingeben"
                txterror.visibility = View.VISIBLE
            }
            if(txtspitzname.trim().isEmpty() && txtusername.trim().isEmpty())
            {
                txterror.text = "Spitzname und Name eingeben"
                txterror.visibility = View.VISIBLE
            }
            else if (txtspitzname.trim().isEmpty()) {
                txterror.text = "Spitzname eingeben"
                txterror.visibility = View.VISIBLE
            }



            else {
                val db = FirebaseFirestore.getInstance() // Reference to Firebase Firestore database

                db.collection("Users")
                    .document(txtspitzname)
                    .get()
                    .addOnSuccessListener { documentSnapshot ->
                        if (documentSnapshot.exists()) {
                            val existingUsername = documentSnapshot.getString("Name")

                            if (existingUsername == txtusername) {
                                // Spitzname und Name sind identisch
                                val intent = Intent(this, Modusauswahl::class.java)
                                intent.putExtra("username", txtusername)
                                startActivity(intent)
                                spitzname.setText("")
                                name.setText("")
                            } else {
                                // Spitzname existiert bereits, aber Name ist unterschiedlich
                                txterror.text = "Falscher Name"
                            }
                        } else {
                            // Spitzname existiert nicht
                            val user = hashMapOf(
                                "Name" to txtusername
                            )

                            db.collection("Users")
                                .document(txtspitzname)
                                .set(user)
                                .addOnSuccessListener {
                                    // Daten wurden erfolgreich hinzugefügt
                                    val intent = Intent(this, Modusauswahl::class.java)
                                    intent.putExtra("username", txtusername)
                                    startActivity(intent)
                                    spitzname.setText("")
                                    name.setText("")
                                }
                                .addOnFailureListener { e ->
                                    // Fehler beim Hinzufügen
                                    txterror.text = "Fehler beim Hinzufügen in die Datenbank."
                                }
                        }
                    }
                    .addOnFailureListener { e ->
                        // Fehler beim Überprüfen des Spitznamens
                        txterror.text = "Fehler beim Überprüfen der Spitznamen-Existenz."
                    }
            }
        }
    }
}
