package de.htw_berlin.quiz_app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
/*import de.htw_berlin.quiz_app.ModusAuswahl
import de.htw_berlin.quiz_app.R
import de.htw_berlin.quiz_app.Spielmodus*/

class Lost : Fragment() {
    private var name: String? = null

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val bundle = arguments
        name = arguments?.getString("name")
        val view = inflater.inflate(R.layout.fragment_lost, container, false)
        val tryAgainButton: Button = view.findViewById(R.id.tryagainbtn)
        tryAgainButton.setOnClickListener {
            Neuesession()
        }

        val zumModusAuswahlButton: Button = view.findViewById(R.id.zum_modus_auswahl)
        zumModusAuswahlButton.setOnClickListener {
            ZumModusAuswahl()
        }

        return view
    }

    private fun Neuesession() {
        val spielmodusFragment = Spielmodus()

        val fragmentManager = requireActivity().supportFragmentManager
        fragmentManager.beginTransaction()
            .replace(R.id.fragment_container, spielmodusFragment)
            .commit()
    }

    private fun ZumModusAuswahl() {

        val modusAuswahlFragment = ModusAuswahl()
        val bundle = Bundle()
        bundle.putString("name", name)
        modusAuswahlFragment.arguments = bundle

        val fragmentManager = requireActivity().supportFragmentManager
        fragmentManager.beginTransaction()
            .replace(R.id.fragment_container, modusAuswahlFragment)
            .commit()
    }

    // ...
}
