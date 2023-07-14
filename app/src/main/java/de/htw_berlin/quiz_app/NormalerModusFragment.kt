@file:Suppress("SpellCheckingInspection")

package de.htw_berlin.quiz_app

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
//import androidx.navigation.NavArgs
//import androidx.navigation.fragment.navArgs
import de.htw_berlin.quiz_app.databinding.FragmentNormalerModusBinding
//import de.htw_berlin.quiz_app.NormalerModusFragmentArgs



//import android.content.IntentSender.OnFinished
//import androidx.lifecycle.get
//import android.os.CountDownTimer

class NormalerModusFragment : Fragment() {
    private lateinit var binding: FragmentNormalerModusBinding    //The binding object that update our layout and inflate it
    private lateinit var normalerModusViewModel: NormalerModusViewModel    //The ViewModel that handles the logic and data of the layout
    private lateinit var category: Category //The passed argument in action

    //Run when this fragment is called
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_normaler_modus, container, false) //Inflate the binding obj
        normalerModusViewModel = ViewModelProvider(this)[NormalerModusViewModel::class.java] //Define the ViewModel
        binding.normalerModusViewModel = normalerModusViewModel //Sets/Bind the ViewModel to work on the fragment
        binding.lifecycleOwner = this //Starts the life cycle of this binding object
        val name = arguments?.getString("name")
        val spitzname = arguments?.getString("spitzname")
        arguments?.let {
            val safeArgs = NormalerModusFragmentArgs.fromBundle(it)
            category = safeArgs.category
        }
        normalerModusViewModel.fetchQuestions(category,requireContext())
        this.setListnerForOptionButtons()
        //To start counting the time
        normalerModusViewModel.startTimer()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        normalerModusViewModel.cancelTimer()
    }

    //Define a click listner for all buttons and calls a method to whatever gets clicked
    private fun setListnerForOptionButtons(){
        //A list of all button that correspond to the answer options
        val optionButtons = listOf(
            binding.option1Button,
            binding.option2Button,
            binding.option3Button,
            binding.option4Button
        )
        optionButtons.forEachIndexed { index, optionButton ->
            optionButton.setOnClickListener {
                normalerModusViewModel.answerQuestion(index)
            }
        }
    }
}