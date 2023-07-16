package de.htw_berlin.quiz_app

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import android.os.CountDownTimer
import android.os.Handler
import android.view.MenuItem
import android.widget.Toast

class Spielmodus : Fragment() {

    private lateinit var fragennummerTextView: TextView
    private lateinit var fragenTextView: TextView
    private lateinit var option1Button: Button
    private lateinit var option2Button: Button
    private lateinit var option3Button: Button
    private lateinit var option4Button: Button
    private lateinit var nextButton: Button


    private var currentQuestionIndex: Int = 0
    private val totalQuestions: Int = 10
    private lateinit var questionsList: MutableList<String>
    private lateinit var suggestionsMap: MutableMap<String, List<String>>
    private lateinit var answersMap: MutableMap<String, String>
    private var currentQuestion: String = ""
    private var currentAnswer: String = ""
    private var userAnswered: Boolean = false
    private var correctAnswers: Int = 0

    private var timeRemaining: Long = 25000 // 25 Sekunden in Millisekunden
    private lateinit var countdownTimer: CountDownTimer
    private lateinit var timerTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        1

        val view = inflater.inflate(R.layout.fragment_spielmodus, container, false)

        fragennummerTextView = view.findViewById(R.id.fragennummer)
        fragenTextView = view.findViewById(R.id.fragen)
        option1Button = view.findViewById(R.id.option1)
        option2Button = view.findViewById(R.id.option2)
        option3Button = view.findViewById(R.id.option3)
        option4Button = view.findViewById(R.id.option4)
        nextButton = view.findViewById(R.id.nextbtn)


        nextButton.isEnabled = false
        timerTextView = view.findViewById(R.id.timer)

        // Initialisierung von Firebase Firestore
        val db = FirebaseFirestore.getInstance()

        // Fragen aus der Sammlung "Questions" in Firebase Firestore abrufen
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val questionsQuerySnapshot = db.collection("Questions").get().await()
                questionsList = mutableListOf<String>()
                suggestionsMap = mutableMapOf<String, List<String>>()
                answersMap = mutableMapOf<String, String>()

                // Durchlaufe die Dokumente in der Sammlung "Questions"
                for (questionDocumentSnapshot: QueryDocumentSnapshot in questionsQuerySnapshot) {
                    val questionText = questionDocumentSnapshot.getString("Text")
                    val suggestions = questionDocumentSnapshot.get("Suggestions") as? List<String>
                    val answer = questionDocumentSnapshot.getString("Answer")

                    questionText?.let { questionsList.add(it) }
                    suggestions?.let { suggestionsMap[questionText!!] = it }
                    answer?.let { answersMap[questionText!!] = it }
                }

                // Mische die Liste der Fragen zufällig
                questionsList.shuffle()

                // Aktualisiere die Fragenummer
                val questionNumberText = "${currentQuestionIndex + 1}/$totalQuestions"
                fragennummerTextView.text = questionNumberText

                // Lade die erste Frage
                loadQuestion()

                // Füge Click Listener für die Optionsbuttons hinzu
                option1Button.setOnClickListener { checkAnswer(option1Button.text.toString()) }
                option2Button.setOnClickListener { checkAnswer(option2Button.text.toString()) }
                option3Button.setOnClickListener { checkAnswer(option3Button.text.toString()) }
                option4Button.setOnClickListener { checkAnswer(option4Button.text.toString()) }

                // Füge Click Listener für den "Weiter"-Button hinzu
                nextButton.setOnClickListener { loadNextQuestion() }

                // Timer konfigurieren
                countdownTimer = object : CountDownTimer(timeRemaining, 1000) {
                    override fun onTick(millisUntilFinished: Long) {
                        timeRemaining = millisUntilFinished
                        val seconds = timeRemaining / 1000
                        val formattedTime = String.format("%02d:%02d", seconds / 60, seconds % 60)
                        timerTextView.text = formattedTime
                    }

                    override fun onFinish() {


                        // Zeit ist abgelaufen, hier kannst du es als automatisch falsche Antwort behandeln oder etwas anderes tun
                        // Rufe die Methode checkAnswer mit einer standardmäßig falschen Antwort auf
                        checkAnswer("")


                    }
                }
                countdownTimer.start()
            } catch (e: Exception) {
                // Fehlerbehandlung beim Abrufen der Fragen aus Firebase
            }
        }

        return view
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                // Comportement du bouton "Up"
                requireActivity().supportFragmentManager.popBackStack()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun loadQuestion() {
        currentQuestion = questionsList[currentQuestionIndex]
        currentAnswer = answersMap[currentQuestion] ?: ""

        fragenTextView.text = currentQuestion

        // Überprüfe, ob Vorschläge für diese Frage verfügbar sind
        if (suggestionsMap.containsKey(currentQuestion) && suggestionsMap[currentQuestion]?.size == 4) {
            val suggestions = suggestionsMap[currentQuestion]!!

            option1Button.text = suggestions[0]
            option2Button.text = suggestions[1]
            option3Button.text = suggestions[2]
            option4Button.text = suggestions[3]

            // Aktiviere die Optionsbuttons
            option1Button.isEnabled = true
            option2Button.isEnabled = true
            option3Button.isEnabled = true
            option4Button.isEnabled = true

            // Setze den Hintergrund der Buttons zurück
            resetButtonStyles()
        } else {
            // Verstecke die Optionsbuttons, wenn keine Vorschläge verfügbar sind
            option1Button.visibility = View.GONE
            option2Button.visibility = View.GONE
            option3Button.visibility = View.GONE
            option4Button.visibility = View.GONE
        }
    }
    // ...

    private fun loadNextQuestion() {
        if (userAnswered) {
            // Überprüfe, ob alle Fragen gestellt wurden
            if (currentQuestionIndex < totalQuestions - 1) {
                currentQuestionIndex++

                // Aktualisiere die Fragenummer
                val questionNumberText = "${currentQuestionIndex + 1}/$totalQuestions"
                fragennummerTextView.text = questionNumberText

                loadQuestion()

                // Setze den Zustand der Benutzerantwort zurück
                userAnswered = false
                nextButton.isEnabled = false

                // Setze den Timer zurück
                countdownTimer.cancel()
                countdownTimer.start()
            } else {
                // Alle Fragen wurden gestellt
                currentQuestionIndex++
                win()

                Toast.makeText(requireContext(), "Quiz abgeschlossen!", Toast.LENGTH_SHORT).show()


            }
        }
    }

    private fun checkAnswer(selectedOption: String) {
        countdownTimer.cancel()
        if (selectedOption == currentAnswer) {
            //Koorekte antwort
            highlightButtonGreen(selectedOption)
            userAnswered = true
            nextButton.isEnabled = true
            correctAnswers++
        }
        else {
            // nicht korrekte
            highlightButtonRed(selectedOption)
            highlightButtonGreen(currentAnswer)
            userAnswered = true
            Handler().postDelayed({
                lost()
            }, 2000) //

        }

        //
        option1Button.isEnabled = false
        option2Button.isEnabled = false
        option3Button.isEnabled = false
        option4Button.isEnabled = false
    }

    private fun highlightButtonGreen(option: String) {
        when (option) {
            option1Button.text -> option1Button.setBackgroundResource(R.drawable.round_back_green)
            option2Button.text -> option2Button.setBackgroundResource(R.drawable.round_back_green)
            option3Button.text -> option3Button.setBackgroundResource(R.drawable.round_back_green)
            option4Button.text -> option4Button.setBackgroundResource(R.drawable.round_back_green)
        }
    }

    private fun highlightButtonRed(option: String) {
        when (option) {
            option1Button.text -> option1Button.setBackgroundResource(R.drawable.round_back_red)
            option2Button.text -> option2Button.setBackgroundResource(R.drawable.round_back_red)
            option3Button.text -> option3Button.setBackgroundResource(R.drawable.round_back_red)
            option4Button.text -> option4Button.setBackgroundResource(R.drawable.round_back_red)
        }
    }

    private fun resetButtonStyles() {
        option1Button.setBackgroundResource(R.drawable.round_back_white)
        option2Button.setBackgroundResource(R.drawable.round_back_white)
        option3Button.setBackgroundResource(R.drawable.round_back_white)
        option4Button.setBackgroundResource(R.drawable.round_back_white)
    }
    private fun lost ()
    {
        nextButton.isEnabled = false
        Handler().postDelayed({
            //
            val lostFragment = Lost()

            val fragmentManager = requireActivity().supportFragmentManager
            fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, lostFragment)
                .commit()
        }, 3000) // Délai de 3 secondes (3000 millisecondes)



    }

    private fun win ()
    {
        nextButton.isEnabled = false
        Handler().postDelayed({
            //
            val winFragment = Win()

            val fragmentManager = requireActivity().supportFragmentManager
            fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, winFragment)
                .commit()
        }, 3000) //
    }




    override fun onDestroyView() {
        super.onDestroyView()
        // Timer stoppen, um Speicherlecks zu vermeiden, wenn das Fragment zerstört wird
        countdownTimer.cancel()
    }
}



