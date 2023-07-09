package de.htw_berlin.quiz_app

import android.content.Context
import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.widget.Toast
//import de.htw_berlin.quiz_app.AnswerSuggestionsList
class NormalerModusViewModel : ViewModel() {

    //DB instance
    private lateinit var _db:DB
    private var questionIndex: Int = 0 // Keeps track of the current question index starting with 0
    private lateinit var questions: List<Question> // Holds all fetched questions


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
            questionIndex++
            if(questionIndex < questions.size){
                displayCurrentQuestion()
            }
            else{
                //TODO: Show final score screen
            }        }
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

//------------------------------Score logic ends here--------------------------------


    //-------------------------------Questions & Options---------------------------------
    //The question text will be saved here
    private  val _questionText = MutableLiveData<String>("")
    //Getter for question text
    val questionText: LiveData<String> get()=_questionText

    //The answer options will be saved here
    private val _options=MutableLiveData<List<String>>()
    //Getter for options
    val options: MutableLiveData<List<String>> get() = _options

    //TODO: Use this to fetchQuestions when creating the fragment
    //This function calls the getRandomQuestions function in the DB class
    fun fetchQuestions(category: Category,context:Context) {
        _db = DB()
        //Call the getRandom question passing the callback function onSuccess
        _db.getRandomQuestions(category, 10, onSuccess = { fetchedQuestions ->
            questions = fetchedQuestions
            if (questionIndex>=questions.size) {
                questionIndex = 0 // Reset the question index to start from the first question
            }
            // Display the first question
            displayCurrentQuestion()
        }, onFailure = {
            Toast.makeText(context,"Unerwarteter Fehler!",Toast.LENGTH_SHORT).show()
        })
    }

    //Display current question according to the questionIndex member of the class
    private fun displayCurrentQuestion() {
        val currentQuestion = questions.getOrNull(questionIndex)
        _questionText.value = currentQuestion?.text?:""//define the var that will be shown in the layout

        val options:List<String> = currentQuestion?.suggestions ?: emptyList()
        _options.value = options
    }


    //This is called to indicate if the pressed button is correct
    fun answerQuestion(buttonClicked: Int) {
        val currentQuestion = questions.getOrNull(questionIndex)

        if (currentQuestion?.answer==currentQuestion?.suggestions?.get(buttonClicked)) {
            _score.value = _score.value?.plus(1)
        }
        questionIndex++
        if(questionIndex < questions.size){
            displayCurrentQuestion()
        }
        else{
            //TODO: Show final score screen
        }
    }
}