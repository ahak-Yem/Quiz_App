package de.htw_berlin.quiz_app

import androidx.lifecycle.MutableLiveData

class AnswerSuggestionsList<T> : MutableLiveData<List<T>>() {
    fun addItem(item: T) {
        val list = value?.toMutableList() ?: mutableListOf()
        list.add(item)
        value = list
    }

    fun removeItem(item: T) {
        val list = value?.toMutableList()
        list?.remove(item)
        value = list
    }

    fun clear() {
        value = emptyList()
    }
}
