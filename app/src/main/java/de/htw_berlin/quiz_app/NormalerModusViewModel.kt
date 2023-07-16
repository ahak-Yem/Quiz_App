package de.htw_berlin.quiz_app

import android.content.Context
import android.os.CountDownTimer
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.widget.Toast
import androidx.fragment.app.FragmentManager

//import de.htw_berlin.quiz_app.AnswerSuggestionsList
class NormalerModusViewModel : ViewModel() {
    private lateinit var _fragmentManager: FragmentManager
    fun setFragmentManager(fragmentManager: FragmentManager) {
        _fragmentManager = fragmentManager
    }
    //DB instance
    private lateinit var _db:DB
    private lateinit var questions: List<Question> // Holds all fetched questions
    private lateinit var _context: Context
    private var questionIndex: Int = 0 // Keeps track of the current question index starting with 0
    private lateinit var _session:Session
    private val _qIndex = MutableLiveData<Int>(1)
    val qIndex: MutableLiveData<Int> get() = _qIndex

    private lateinit var _spitzname:String
    fun setNickname(nickname:String){
        _spitzname=nickname
    }

    //------------------------------------Timer------------------------------------------
    //Saves the timer value
    private var _timer = MutableLiveData<Long>()
    //Getter for the timer to be called on the layout
    val timer: MutableLiveData<Long> get() = _timer

    //Handles the timer count down value, starts with 30000ms and counts down 1000ms everytime
    private var countDownTimer: CountDownTimer = object : CountDownTimer(30000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            // Update the timer value
            _timer.value = millisUntilFinished / 1000
        }

        override fun onFinish() {
            questionIndex++
            _qIndex.value = questionIndex + 1
            if (questionIndex < questions.size) {
                _db.updateSession(_session, false, questions[questionIndex - 1].id, false, onSuccess = { returnedSession ->
                    _session = returnedSession
                    Toast.makeText(_context, "Timeout!", Toast.LENGTH_SHORT).show()
                }, onFailure = {
                    Toast.makeText(_context, "Nicht gespeichert", Toast.LENGTH_SHORT).show()
                })
                displayCurrentQuestion()
            } else {
                _db.updateSession(_session, false, questions[questionIndex - 1].id, true, onSuccess = { returnedSession ->
                    _session = returnedSession
                    Toast.makeText(_context, "Timeout & Fertig!", Toast.LENGTH_SHORT).show()
                }, onFailure = {
                    Toast.makeText(_context, "Nicht gespeichert", Toast.LENGTH_SHORT).show()
                })
                cancelTimer()
                val dialogFragment = QuizCompletedDialogFragment()
                dialogFragment.show(_fragmentManager, "quiz_completed_dialog")
            }
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
    private var _score = MutableLiveData<Int>(0)
    //Getter of score for layout
    val score: MutableLiveData<Int> get()=_score

//------------------------------Score logic ends here--------------------------------


    //-------------------------------Questions & Options---------------------------------
    //The question text will be saved here
    private  var _questionText = MutableLiveData<String>("")
    //Getter for question text
    val questionText: MutableLiveData<String> get()=_questionText

    //The answer options will be saved here
    private var _options=MutableLiveData<List<String>>()
    //Getter for options
    val options: MutableLiveData<List<String>> get() = _options

    //This function calls the getRandomQuestions function in the DB class
    fun fetchQuestions(category: Category,context:Context) {
        _db = DB()
        _context=context
        //Call the getRandom question passing the callback function onSuccess
        _db.getRandomQuestions(category, 10, onSuccess = { fetchedQuestions ->
            questions = fetchedQuestions
            if (questionIndex>=questions.size) {
                _qIndex.value=1
                questionIndex = 0 // Reset the question index to start from the first question
                _score= MutableLiveData<Int>(0)
            }
            _db.startSession(_spitzname,questions,category,"Normaler Modus", onSuccess = { returnedSession ->
                _session=returnedSession
                Toast.makeText(context,"Session startet!",Toast.LENGTH_SHORT).show()
            }, onFailure = {
                    Toast.makeText(context,"Nicht gespeichert",Toast.LENGTH_SHORT).show()
                })
            // Display the first question
            displayCurrentQuestion()
        }, onFailure = {
            Toast.makeText(context,"Unerwarteter Fehler!",Toast.LENGTH_SHORT).show()
        })
    }

    //Display current question according to the questionIndex member of the class
    private fun displayCurrentQuestion() {
        startTimer()
        if (questions.isNotEmpty() && questionIndex < questions.size) {
            val currentQuestion = questions.getOrNull(questionIndex)
            _questionText.value = currentQuestion?.text?:""//define the var that will be shown in the layout

            val options:List<String> = currentQuestion?.suggestions ?: emptyList()
            _options.value = options
        }
        else{
            cancelTimer()
            Toast.makeText(_context,"Empty List",Toast.LENGTH_SHORT).show()
        }
    }


    fun answerQuestion(buttonClicked: Int) {
        val currentQuestion = questions.getOrNull(questionIndex)
        if (currentQuestion?.answer == currentQuestion?.suggestions?.get(buttonClicked)) {
            _score.value = _score.value?.plus(1)
            if (questionIndex < questions.size - 1) {
                if (currentQuestion != null) {
                    _db.updateSession(_session, true, currentQuestion.id, false, onSuccess = { returnedSession ->
                        _session = returnedSession
                        Toast.makeText(_context, "Richtig!", Toast.LENGTH_SHORT).show()
                        questionIndex++
                        _qIndex.value = questionIndex + 1
                        displayCurrentQuestion()
                    }, onFailure = {
                        Toast.makeText(_context, "Nicht gespeichert", Toast.LENGTH_SHORT).show()
                    })
                }
            } else {
                if (currentQuestion != null) {
                    _db.updateSession(_session, true, currentQuestion.id, true, onSuccess = { finalSession ->
                        _session = finalSession
                        Toast.makeText(_context, "Richtig & Fertig!", Toast.LENGTH_SHORT).show()
                        questionIndex++
                        cancelTimer()
                        val dialogFragment = QuizCompletedDialogFragment()
                        dialogFragment.show(_fragmentManager, "quiz_completed_dialog")
                    }, onFailure = {
                        Toast.makeText(_context, "Nicht gespeichert", Toast.LENGTH_SHORT).show()
                    })
                }
            }
        } else {
            if (currentQuestion != null) {
                _db.updateSession(_session, false, currentQuestion.id, false, onSuccess = { returnedSession ->
                    _session = returnedSession
                    Toast.makeText(_context, "Falsch!", Toast.LENGTH_SHORT).show()
                    questionIndex++
                    _qIndex.value = questionIndex + 1
                    if (questionIndex >= questions.size) {
                        cancelTimer()
                        _qIndex.value = 10
                        val dialogFragment = QuizCompletedDialogFragment()
                        dialogFragment.show(_fragmentManager, "quiz_completed_dialog")
                    }
                    displayCurrentQuestion()
                }, onFailure = {
                    Toast.makeText(_context, "Nicht gespeichert", Toast.LENGTH_SHORT).show()
                })
            }
        }
    }

}
