package com.shan.library.ui.bookdetail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.shan.library.model.DetailedBook
import com.shan.library.repository.LibraryRepository
import com.shan.library.services.RetrofitServiceBuilder
import kotlinx.coroutines.launch

class BookDetailViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: LibraryRepository = LibraryRepository(RetrofitServiceBuilder.retrofitService)

    private val _book = MutableLiveData<DetailedBook>()
    val book: LiveData<DetailedBook> get() = _book

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    fun fetchBookDetails(bookId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val bookDetails = repository.getDetailedBookById(bookId)
                _book.value = bookDetails
            } catch (e: Exception) {
                _error.value = "Failed to fetch book details: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateBook(bookId: Int, bookName: String, isbn: Int) {
        viewModelScope.launch {
            try {
                repository.updateBook(bookId, bookName, isbn)
                fetchBookDetails(bookId)  // Refresh the book details
            } catch (e: Exception) {
                _error.value = "Failed to fetch book details: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateAuthor(bookId: Int,authorId: Int, firstName: String, lastName: String) {
        viewModelScope.launch {
            try {
                repository.updateAuthor(authorId, firstName, lastName)
                fetchBookDetails(bookId)  // Refresh the book details
            } catch (e: Exception) {
                _error.value = "Failed to fetch book details: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}