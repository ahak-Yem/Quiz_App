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
/*import de.htw_berlin.quiz_app.ModusAuswahl
import de.htw_berlin.quiz_app.R*/

class Anmeldung : Fragment() {
    private var nameEditText: EditText? = null
    private var spitznameEditText: EditText? = null
    private var errorTextView: TextView? = null
    private lateinit var db: FirebaseFirestore
    private var _spitzname: String? = null

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
                val sharedPreferences = requireActivity().getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putString("spitzname", spitzname)
                editor.apply()
                _spitzname = spitzname

                docRef.get()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val document = task.result
                            if (document != null && document.exists()) {
                                val storedName = document.getString("Name")
                                if (storedName == name) {
                                    // Cas 1: Utilisateur existant avec le bon nom
                                    saveLoginState(name)
                                    redirectToModusAuswahl(name)
                                } else {
                                    // Cas 3: Utilisateur existant avec un mauvais nom
                                    showError("Bitte wählen Sie einen anderen Spitzname ein.")
                                }
                            } else {
                                // Cas 2: Nouvel utilisateur
                                val data = hashMapOf(
                                    "Name" to name
                                )

                                db.collection("Users").document(spitzname).set(data)
                                    .addOnSuccessListener {
                                        saveLoginState(name)
                                        redirectToModusAuswahl(name)
                                    }
                                    .addOnFailureListener {
                                        showError("Fehler beim Registrieren des Benutzers.")
                                    }
                            }
                        } else {
                            // Erreur lors de l'accès à la base de données
                            showError("Fehler beim Zugriff auf die Datenbank.")
                        }
                    }
            } else {
                showError("Bitte geben Sie Name und Spitzname ein.")
            }
        }

        val sharedPreferences = requireActivity().getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)

        if (isLoggedIn) {
            // Utilisateur déjà connecté, rediriger vers le fragment ModusAuswahl
            val name = sharedPreferences.getString("name", "") ?: ""
            val editor = sharedPreferences.edit()
            _spitzname=sharedPreferences.getString("spitzname", "") ?: ""
            editor.putString("spitzname",_spitzname)
            editor.apply()
            redirectToModusAuswahl(name)
        }

        return view
    }

    private fun saveLoginState(name: String) {
        val sharedPreferences = requireActivity().getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("isLoggedIn", true)
        editor.putString("name", name)
        editor.putString("spitzname",_spitzname)
        editor.apply()
    }

    private fun redirectToModusAuswahl(name: String) {
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

    private fun showError(errorMessage: String) {
        errorTextView?.text = errorMessage
        errorTextView?.visibility = View.VISIBLE
    }
}
