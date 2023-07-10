package de.htw_berlin.quiz_app
import com.google.firebase.firestore.DocumentReference

//data class Question. Sieht wie die Firestore Dokument aus.
data class Question(
    val id: String,
    val text: String?,
    val category: DocumentReference?,
    val level:String?,
    val suggestions: List<String>?,
    val answer: String?
    )