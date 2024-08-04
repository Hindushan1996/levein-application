package com.shan.library.ui.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.shan.library.model.Book
import com.shan.library.repository.LibraryRepository
import com.shan.library.services.RetrofitServiceBuilder
import kotlinx.coroutines.launch


class BookListViewModel (application: Application) : AndroidViewModel(application) {
    private val repository: LibraryRepository

    private val _books = MutableLiveData<List<Book>>()
    val books: LiveData<List<Book>> get() = _books

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    init {
        val apiService = RetrofitServiceBuilder.retrofitService
        repository = LibraryRepository(apiService)
        fetchBooks()
    }

    /**
     * Fetch books from the repository and update the LiveData properties.
     */
    private fun fetchBooks() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                _books.value = repository.getAllBooks()
            } catch (e: Exception) {
                _error.value = "Failed to fetch books: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}