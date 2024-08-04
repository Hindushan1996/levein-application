package com.shan.library.ui.addbook

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.shan.library.model.Author
import com.shan.library.model.CreateBook
import com.shan.library.repository.LibraryRepository
import com.shan.library.services.RetrofitServiceBuilder
import kotlinx.coroutines.launch

class AddBookViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: LibraryRepository

    private val _authors = MutableLiveData<List<Author>>()
    val authors: LiveData<List<Author>> get() = _authors

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    private val _bookCreated = MutableLiveData<Boolean>()
    val bookCreated: LiveData<Boolean> get() = _bookCreated

    init {
        val apiService = RetrofitServiceBuilder.retrofitService
        repository = LibraryRepository(apiService)
        fetchAuthors()
    }

    /**
     * Fetch all authors from the repository.
     */
    private fun fetchAuthors() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                _authors.value = repository.getAllAuthor()
            } catch (e: Exception) {
                _error.value = "Failed to fetch authors: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Add a new book to the repository.
     */
    fun addBook(bookName: String, isbn: Long, authorId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val book = CreateBook( bookName, isbn, authorId)
                repository.addBook(book)
                _bookCreated.value = true
            } catch (e: Exception) {
                _error.value = "Failed to add book: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Reset the bookCreated state after navigation.
     */
    fun onNavigated() {
        _bookCreated.value = false
    }
}