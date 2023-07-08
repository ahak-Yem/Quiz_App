package de.htw_berlin.quiz_app

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
//import de.htw_berlin.quiz_app.AnswerSuggestionsList
class NormalerModusViewModel : ViewModel() {

//------------------------------------Timer------------------------------------------
    //Saves the timer value
    private val _timer = MutableLiveData<Long>()
    //Getter for the timer to be called on the layout
    val timer: LiveData<Long> get() = _timer

    //Handles the timer count down value, starts with 30000ms and counts down 1000ms everytime
    private val countDownTimer: CountDownTimer = object : CountDownTimer(30000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            // Update the timer value
            _timer.value = millisUntilFinished / 1000
        }

        override fun onFinish() {
            TODO("call next question and its data")
        }
    }

    //starts the timer from the beginning
    fun startTimer(){
        countDownTimer.start()
    }

    //Cancel the timer at the end.
    fun cancelTimer() {
        countDownTimer.cancel()
    }
//------------------------------Timer logic ends here--------------------------------


//-----------------------------------Score-------------------------------------------
    //Saves the score value and it will start with 0
    private val _score = MutableLiveData<Int>(0)
    //Getter of score for layout
    val score: LiveData<Int> get()=_score

    //This is called to indicate if the pressed button is correct
    fun answerQuestion(buttonClicked: Int) {
        TODO("build checking logic following example below")
        if (buttonClicked==1) {
            _score.value = (_score.value ?: 0) + 1
        }
    }
//------------------------------Score logic ends here--------------------------------

    //The question text will be saved here
    private  val _questionText = MutableLiveData<String>("")
    //Getter for question text
    val questionText: LiveData<String> get()=_questionText

    //The answer options will be saved here
    private val _options=AnswerSuggestionsList<String>()

    //Getter for options
    val options: LiveData<List<String>> get() = _options

}