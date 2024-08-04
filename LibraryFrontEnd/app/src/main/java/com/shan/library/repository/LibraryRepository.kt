package com.shan.library.repository

import com.shan.library.model.AuthorUpdateRequest
import com.shan.library.model.BookUpdateRequest
import com.shan.library.model.CreateAuthor
import com.shan.library.model.CreateBook
import com.shan.library.services.RetrofitServices

class LibraryRepository (private val apiService: RetrofitServices) {

    suspend fun getAllBooks() = apiService.getAllBooks()

    suspend fun createAuthor(createAuthor: CreateAuthor) = apiService.createAuthor(createAuthor)

    suspend fun getAllAuthor() = apiService.getAllAuthors()

    suspend fun addBook(addBook: CreateBook) = apiService.addBook(addBook)

    suspend fun getDetailedBookById(bookId: Int) = apiService.getDetailedBookById(bookId)

    suspend fun updateBook(bookId: Int, bookName: String, isbn: Int) {
        val request = BookUpdateRequest(bookName, isbn)
        apiService.updateBook(bookId, request)
    }

    suspend fun updateAuthor(authorId: Int, firstName: String, lastName: String) {
        val request = AuthorUpdateRequest(firstName, lastName)
        apiService.updateAuthor(authorId, request)
    }
}