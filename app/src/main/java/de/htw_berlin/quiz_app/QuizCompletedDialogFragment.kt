package de.htw_berlin.quiz_app

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class QuizCompletedDialogFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView = inflater.inflate(R.layout.fragment_quiz_completed_dialog, container, false)

        // Set click listeners for the buttons in the dialog
        rootView.findViewById<Button>(R.id.restartButton).setOnClickListener {
            findNavController().popBackStack(R.id.categorySelectionFragment, false)
            dismiss()
        }


        return rootView
    }
}

