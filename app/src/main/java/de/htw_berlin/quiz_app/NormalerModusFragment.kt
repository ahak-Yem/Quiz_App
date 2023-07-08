@file:Suppress("SpellCheckingInspection")

package de.htw_berlin.quiz_app

//import android.content.IntentSender.OnFinished
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
//import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
//import androidx.lifecycle.get
import de.htw_berlin.quiz_app.databinding.FragmentNormalerModusBinding

class NormalerModusFragment : Fragment() {
    //The binding object that update our layout and inflate it
    private lateinit var binding: FragmentNormalerModusBinding
    //The ViewModel that handles the logic and date of the layout
    private lateinit var normalerModusViewModel: NormalerModusViewModel


    //TODO(Can be deleted later?)
    companion object {
        //fun newInstance() = NormalerModusFragment()
    }
    //Run when this fragment is called
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_normaler_modus, container, false)//Inflate the binding obj
        normalerModusViewModel = ViewModelProvider(this).get(NormalerModusViewModel::class.java)//Define the ViewModel
        binding.normalerModusViewModel = normalerModusViewModel//Sets/Bind the ViewModel to work on the fragment
        binding.lifecycleOwner = this //Starts the life cycle of this binding object

        //To start counting the time
        normalerModusViewModel.startTimer()
        this.setListnerForOptionButtons()
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
        optionButtons.forEach{optionButton->
            optionButton.setOnClickListener {
                val optionTag = it.tag as Int
                normalerModusViewModel.answerQuestion(optionTag)
            }
        }
    }
}