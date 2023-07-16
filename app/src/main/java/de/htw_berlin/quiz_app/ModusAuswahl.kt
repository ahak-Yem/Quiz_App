package de.htw_berlin.quiz_app


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction



class ModusAuswahl : Fragment() {
    private var nameTextView: TextView? = null
    private var hilfeTextView: TextView? = null
    //private lateinit var navController:NavController


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_modus_auswahl, container, false)

        nameTextView = view.findViewById(R.id.willkommen)

        val name = arguments?.getString("name")
        name?.let {
            nameTextView?.text = "Hallo $it"
        }

        val normalModeButton = view.findViewById<Button>(R.id.normalMode)
        normalModeButton.setOnClickListener {
            val intent = Intent(requireActivity(), CategorySelectionActivity::class.java)
            startActivity(intent)
        }//TODO: This should be removed after achieving navigation using NavGraph
        val spielmodusButton = view.findViewById<Button>(R.id.spielmodusButton)
        spielmodusButton.setOnClickListener {
            val fragment = Spielmodus()
            val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
            val transaction: FragmentTransaction = fragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
        hilfeTextView = view.findViewById(R.id.hilfe)
        hilfeTextView?.setOnClickListener {

            showDialog()
        }

        return view
    }
    override fun onResume() {
        super.onResume()
        // Get the MainActivity and access its bottomNavigationView
        val mainActivity = requireActivity() as? MainActivity
        mainActivity?.bottomNavigationView?.visibility = View.VISIBLE
    }
    private fun showDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("A Propos Modus")
        builder.setMessage("SpielModus: 10 Fragen werden zufällig aus verschiedenen Kategorien gestellt. \n" +
                "Das Spielprinzip ist wie folgt\n" +
                "eine falsche Antwort -> Spiel verloren\n" +
                "Zeit läuft ab -> Spiel verloren\n" +
                "alle Fragen wurden beantwortet -> Winner :).\n\n NormalModus aus Kategorien und da wird ungefahr 10 Pro Kategorie fragen gestellt ")
        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }






}