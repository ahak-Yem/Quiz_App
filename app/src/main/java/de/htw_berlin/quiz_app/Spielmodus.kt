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

class Spielmodus : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var fragenTextView: TextView
    private lateinit var topicNameTextView: TextView
    private lateinit var option1Button: Button
    private lateinit var option2Button: Button
    private lateinit var option3Button: Button
    private lateinit var option4Button: Button





    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_spielmodus, container, false)

        fragenTextView = view.findViewById(R.id.fragen)
        option1Button = view.findViewById(R.id.option1)
        option2Button = view.findViewById(R.id.option2)
        option3Button = view.findViewById(R.id.option3)
        option4Button = view.findViewById(R.id.option4)


        // Initialisation de Firebase Firestore
        val db = FirebaseFirestore.getInstance()

        // Récupérer les questions depuis la collection "Questions" dans Firebase Firestore
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val questionsQuerySnapshot = db.collection("Questions").get().await()
                val questionsList = mutableListOf<String>()
                val suggestionsMap = mutableMapOf<String, List<String>>()

                // Parcourir les documents de la collection "Questions"
                for (questionDocumentSnapshot: QueryDocumentSnapshot in questionsQuerySnapshot) {
                    val questionText = questionDocumentSnapshot.getString("Text")
                    val suggestions = questionDocumentSnapshot.get("Suggestions") as? List<String>

                    questionText?.let { questionsList.add(it) }
                    suggestions?.let { suggestionsMap[questionText!!] = it }
                }

                // Mélanger la liste de questions de manière aléatoire
                questionsList.shuffle()

                // Mettre à jour le TextView avec la première question (indice 0)
                if (questionsList.isNotEmpty()) {
                    val currentQuestion = questionsList[0]
                    fragenTextView.text = currentQuestion

                    // Vérifier si les suggestions de réponses sont disponibles pour cette question
                    if (suggestionsMap.containsKey(currentQuestion) && suggestionsMap[currentQuestion]?.size == 4) {
                        val suggestions = suggestionsMap[currentQuestion]!!

                        option1Button.text = suggestions[0]
                        option2Button.text = suggestions[1]
                        option3Button.text = suggestions[2]
                        option4Button.text = suggestions[3]
                    } else {
                        // Cacher les boutons d'option si les suggestions ne sont pas disponibles
                        option1Button.visibility = View.GONE
                        option2Button.visibility = View.GONE
                        option3Button.visibility = View.GONE
                        option4Button.visibility = View.GONE
                    }
                }
            } catch (e: Exception) {
                // Gérer les erreurs lors de la récupération des questions depuis Firebase
            }
        }

        return view

    }


}