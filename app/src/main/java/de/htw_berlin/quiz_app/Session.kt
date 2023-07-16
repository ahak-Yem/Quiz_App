package de.htw_berlin.quiz_app
import com.google.firebase.Timestamp

data class Session (
        @Transient
        val id:String,
        val UserID: String,
        val Count_Correct: Long?,
        val Last_Answered_Question: String?,
        val Category: String?,
        val Mode:String?,
        val Started_At: Timestamp,
        @field:JvmField
        val isComplete: Boolean?,
        val Questions: ArrayList<String>?
    )