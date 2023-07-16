package de.htw_berlin.quiz_app

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SessionsViewModel : ViewModel() {
    private val _sessions = MutableLiveData<List<Session>>()
    val sessions: LiveData<List<Session>> get() = _sessions
    private lateinit var _db:DB

    // Function to fetch user-specific sessions
    fun getUserSessions(userID: String, onSuccess: (Int) -> Unit, onFailure: (Exception) -> Unit) {
        _db=DB()
        // Call the Firestore helper method to fetch user sessions
        _db.getUserSessions(
            userID,
            onSuccess = { sessions ->
                _sessions.value = sessions // Update the LiveData with the fetched sessions
                onSuccess(sessions.size)
            },
            onFailure = { exception ->
                    onFailure(exception)
            }
        )
    }
}