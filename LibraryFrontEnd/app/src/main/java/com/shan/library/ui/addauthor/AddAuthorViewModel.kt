package com.shan.library.ui.addauthor

import android.app.Application
import com.shan.library.services.RetrofitServiceBuilder

import androidx.lifecycle.*
import com.shan.library.model.CreateAuthor
import com.shan.library.repository.LibraryRepository
import kotlinx.coroutines.launch

class AddAuthorViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: LibraryRepository

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    private val _authorCreated = MutableLiveData<Boolean>()
    val authorCreated: LiveData<Boolean> get() = _authorCreated

    init {
        val apiService = RetrofitServiceBuilder.retrofitService
        repository = LibraryRepository(apiService)
    }

    /**
     * Creates a new author using the provided first name and last name.
     */
    fun createAuthor(firstName: String, lastName: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val createAuthor = CreateAuthor(firstName = firstName, lastName = lastName)
                repository.createAuthor(createAuthor)
                _authorCreated.value = true
            } catch (e: Exception) {
                _error.value = "Failed to create author: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Resets the author creation state after navigation.
     */
    fun onNavigated() {
        _authorCreated.value = false
    }
}