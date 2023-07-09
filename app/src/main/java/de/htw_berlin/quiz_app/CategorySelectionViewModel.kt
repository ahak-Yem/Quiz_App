package de.htw_berlin.quiz_app
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class CategoryViewModel : ViewModel() {
    private val db: DB = DB()
    private val _categories: MutableLiveData<MutableList<Category>> = MutableLiveData()
    val categories: LiveData<MutableList<Category>> get() = _categories
    private val _error: MutableLiveData<String> = MutableLiveData()
    val error: LiveData<String> get() = _error

    fun setCategories() {
        db.getCategories(
            onSuccess = { categoryList ->
                _categories.value = categoryList as MutableList<Category>
            },
            onFailure = { exception ->
                _error.value="Failed to open the next screen!"
            }
        )
    }
}
