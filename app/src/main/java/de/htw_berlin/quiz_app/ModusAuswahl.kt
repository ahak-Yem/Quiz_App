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
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction


class ModusAuswahl : Fragment() {
    // TODO: Rename and change types of parameters
    private var nameTextView: TextView? = null
    private var hilfeTextView: TextView? = null



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_modus_auswahl, container, false)

        nameTextView = view.findViewById(R.id.willkommen)

        val name = arguments?.getString("name")
        nameTextView?.text = "Hallo $name"
        val normalModeButton = view.findViewById<Button>(R.id.normalMode)
        normalModeButton.setOnClickListener {
            val intent = Intent(requireActivity(), CategorySelectionActivity::class.java)
            startActivity(intent)
        }
        val spielmodusButton = view.findViewById<Button>(R.id.spielmodusButton)
        spielmodusButton.setOnClickListener {
            val fragment = Spielmodus() // Remplacez par le fragment Spielmodus
            val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
            val transaction: FragmentTransaction = fragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
        hilfeTextView = view.findViewById(R.id.hilfe)
        hilfeTextView?.setOnClickListener {
            // Action à effectuer lorsque le TextView est cliqué
            showDialog()
        }

        return view
    }
    private fun showDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("A Propos Modus")
        builder.setMessage("Spielmodus ist einfach ein Randon von Fragen aus verschiedenen Kategorien.\n\n NormalModus aus Kategorien und da wird ungefahr 10 fragen gestellt")
        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }





}