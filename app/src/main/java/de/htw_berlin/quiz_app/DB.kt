package de.htw_berlin.quiz_app
import com.google.firebase.Timestamp
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Date
import kotlin.random.Random

//This class handles communication with DB to fetch the collections
class DB {
    //A private attribute field with datatype(FirebaseFirestore) that saves our firestore instance
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    //A function to get all categories from firestore, saves it in a list and pass it
    //using the onSuccess(list) callback function if failed then callback exception function.
    fun getCategories(onSuccess: (List<Category>) -> Unit, onFailure: (Exception) -> Unit) {

        //Name of our Collection on firestore
        val categoriesCollection = firestore.collection("Categories")

        //Trying to get all the documents
        //If successful then we will play with the queryResult=collection
        categoriesCollection.get().addOnSuccessListener { queryResult ->
            val categoryList = mutableListOf<Category>()

            //foreach document in our collection
            for (document in queryResult.documents) {

                //Get the document id and the 2 fields from the document.
                val categoryId = document.id
                val categoryName = document.getString("Name") ?: ""
                val categoryIconUrl = document.getString("IconPath") ?: ""

                //Save the document data as an object of the data class "Category" see Category.kt
                categoryList.add(Category(categoryId, categoryName, categoryIconUrl))
            }
            onSuccess(categoryList)
        }.addOnFailureListener { exception ->
            onFailure(exception)
        }
    }
    //This function fetches all questions from the db filter with category and choose random 10 questions from them
    //Using the onSuccess(list) callback function we will pass the questions
    fun getRandomQuestions(category: Category,selectCount:Int , onSuccess: (List<Question>) -> Unit,onFailure: (Exception) -> Unit) {
        val questionsList= mutableListOf<Question>()
        firestore.collection("Questions")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val questionId = document.id
                    val questionLevel = document.data["Level"] as String
                    val questionCategory = document.data["Category"] as? DocumentReference
                    val questionText = document.data["Text"] as String
                    val suggestions = document.data["Suggestions"] as? List<String>
                    val answer = document.data["Answer"] as String
                    if(category.name=="Geographies" && questionCategory?.id == "GeographyCat"){
                        questionsList.add(Question(questionId,questionText,questionCategory,questionLevel,suggestions,answer))
                    }
                    else if(category.name=="Programmierung" && questionCategory?.id == "ProgrammierungCat"){
                        questionsList.add(Question(questionId,questionText,questionCategory,questionLevel,suggestions,answer))
                    }
                    else if(category.name=="Sport" && questionCategory?.id == "SportsCat"){
                        questionsList.add(Question(questionId,questionText,questionCategory,questionLevel,suggestions,answer))
                    }
                    else if(category.name=="Mathe" && questionCategory?.id == "MathCat"){
                        questionsList.add(Question(questionId,questionText,questionCategory,questionLevel,suggestions,answer))
                    }
                    else if(category.name=="Musik" && questionCategory?.id == "MusicCat"){
                        questionsList.add(Question(questionId,questionText,questionCategory,questionLevel,suggestions,answer))
                    }
                }
                val finalQuestions= selectRandomQuestions(questionsList,selectCount)
                onSuccess(finalQuestions)
            }
            .addOnFailureListener { exception ->
                    onFailure(exception)
            }
    }

    //This method selects 10 random questions
    private fun selectRandomQuestions(questions: List<Question>, count: Int): List<Question> {
        return if (count < questions.size && count > 0) {
            val randomIndices = mutableListOf<Int>()
            while (randomIndices.size < count) {
                val randomIndex = Random.nextInt(1, questions.size + 1)
                val questionId = "Q$randomIndex"
                val index = questions.indexOfFirst { it.id == questionId }
                if (index != -1 && index !in randomIndices) {
                    randomIndices.add(index)
                }
            }
            randomIndices.map { questions[it] }
        } else {
            questions
        }
    }

    fun startSession(spitzname:String, questions: List<Question>, category: Category, mode:String):Session{
        val questionID=mutableListOf<String>()
        for (question in questions){
            questionID.add(question.id)
        }
        val countOfDocuments= firestore.collection("Sessions").count().get(AggregateSource.SERVER).result.count+1
        val session= Session(
            "Session$countOfDocuments",
            spitzname,
            0,
            "nothing",
            category.name,
            mode,
            Timestamp(Date()),
            false,
            questionID as ArrayList<String>
        )
        firestore.collection("Sessions").document("Session$countOfDocuments").set(session)
        return session
    }
    fun updateSession(session:Session,incrementScore:Boolean,lastAnsweredQuestion:String,isComplete:Boolean):Session{
        var currentScore=session.Count_Correct
        if(incrementScore){
            currentScore = currentScore!! + 1
        }
        val newSession=Session(
            session.id,
            session.UserID,
            currentScore,
            lastAnsweredQuestion,
            session.Category,
            session.Mode,
            session.Started_At,
            isComplete,
            session.Questions
        )

        val currentSessionDoc = firestore.collection("Sessions").document(session.id)
        currentSessionDoc.set(newSession)
        return newSession
    }
}