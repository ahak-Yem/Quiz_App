package de.htw_berlin.quiz_app

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore


class Anmeldung : Fragment() {
    private var nameEditText: EditText? = null
    private var spitznameEditText: EditText? = null
    private var errorTextView: TextView? = null
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(requireContext())
        db = FirebaseFirestore.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_anmeldung, container, false)

        nameEditText = view.findViewById(R.id.Username)
        spitznameEditText = view.findViewById(R.id.Spitzname)
        errorTextView = view.findViewById(R.id.textinput_error)

        val startButton = view.findViewById<Button>(R.id.Start_btn)
        startButton.setOnClickListener {
            val name = nameEditText?.text.toString()
            val spitzname = spitznameEditText?.text.toString()

            if (name.isNotEmpty() && spitzname.isNotEmpty()) {
                val docRef = db.collection("Users").document(spitzname)

                docRef.get()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val document = task.result
                            if (document != null && document.exists()) {
                                val storedName = document.getString("Name")
                                if (storedName == name) {
                                    // Cas 1: Utilisateur existant avec le bon nom
                                    val fragment = ModusAuswahl()
                                    val bundle = Bundle()
                                    bundle.putString("name", name)
                                    fragment.arguments = bundle
                                    val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
                                    val transaction: FragmentTransaction = fragmentManager.beginTransaction()
                                    transaction.replace(R.id.fragment_container, fragment)
                                    transaction.addToBackStack(null)
                                    transaction.commit()
                                } else {
                                    // Cas 3: Utilisateur existant avec un mauvais nom
                                    errorTextView?.text = "Bitte wählen Sie einen anderen Spitzname ein."
                                    errorTextView?.visibility = View.VISIBLE
                                }
                            } else {
                                // Cas 2: Nouvel utilisateur
                                val data = hashMapOf(
                                    "Name" to name
                                )

                                db.collection("Users").document(spitzname).set(data)
                                    .addOnSuccessListener {
                                        val fragment = ModusAuswahl()
                                        val bundle = Bundle()
                                        bundle.putString("name", name)
                                        fragment.arguments = bundle
                                        val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
                                        val transaction: FragmentTransaction = fragmentManager.beginTransaction()
                                        transaction.replace(R.id.fragment_container, fragment)
                                        transaction.addToBackStack(null)
                                        transaction.commit()
                                    }
                                    .addOnFailureListener {
                                        errorTextView?.text = "Fehler beim Registrieren des Benutzers."
                                        errorTextView?.visibility = View.VISIBLE
                                    }
                            }
                        } else {
                            // Erreur lors de l'accès à la base de données
                            errorTextView?.text = "Fehler beim Zugriff auf die Datenbank."
                            errorTextView?.visibility = View.VISIBLE
                        }
                    }
            } else {
                errorTextView?.text = "Bitte geben Sie Name und Spitzname ein."
                errorTextView?.visibility = View.VISIBLE
            }
        }
        val sharedPreferences = requireActivity().getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)

        if (isLoggedIn) {
            // Utilisateur déjà connecté, rediriger vers le fragment ModusAuswahl
            val name = sharedPreferences.getString("name", "") ?: ""
            val fragment = ModusAuswahl()
            val bundle = Bundle()
            bundle.putString("name", name)
            fragment.arguments = bundle
            val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
            val transaction: FragmentTransaction = fragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        return view
    }


}