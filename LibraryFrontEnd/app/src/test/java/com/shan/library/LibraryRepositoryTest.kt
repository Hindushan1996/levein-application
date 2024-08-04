package com.shan.library

import com.shan.library.model.Author
import com.shan.library.model.Book
import com.shan.library.model.CreateAuthor
import com.shan.library.model.CreateBook
import com.shan.library.model.DetailedBook
import com.shan.library.repository.LibraryRepository
import com.shan.library.services.RetrofitServices
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

class LibraryRepositoryTest {

    private lateinit var retrofitService: RetrofitServices
    private lateinit var repository: LibraryRepository

    @Before
    fun setUp() {
        retrofitService = mock(RetrofitServices::class.java)
        repository = LibraryRepository(retrofitService)
    }

    @Test
    fun testGetAllBooks(): Unit = runBlocking {
        val mockBooks = listOf(
            Book(1, "Book 1", 123,1),
            Book(2, "Book 2", 456,1)
        )
        `when`(retrofitService.getAllBooks()).thenReturn(mockBooks)

        val books = repository.getAllBooks()

        assertEquals(mockBooks, books)
        verify(retrofitService).getAllBooks()
    }

    @Test
    fun testCreateAuthor() = runBlocking {
        val newAuthor = CreateAuthor("John", "Doe")
        repository.createAuthor(newAuthor)

        verify(retrofitService).createAuthor(newAuthor)
    }

    @Test
    fun testGetAllAuthors(): Unit = runBlocking {
        val mockAuthors = listOf(
            Author(1, "John", "Doe"),
            Author(2, "Jane", "Doe")
        )
        `when`(retrofitService.getAllAuthors()).thenReturn(mockAuthors)

        val authors = repository.getAllAuthor()

        assertEquals(mockAuthors, authors)
        verify(retrofitService).getAllAuthors()
    }

    @Test
    fun testAddBook() = runBlocking {
        val newBook = CreateBook("New Book", 789, 1)
        repository.addBook(newBook)

        verify(retrofitService).addBook(newBook)
    }

    @Test
    fun testGetDetailedBookById(): Unit = runBlocking {
        // Mock the response
        val mockAuthor = Author(id = 1, firstName = "John", lastName = "Doe")
        val mockDetailedBook = DetailedBook(id = 1, bookName = "Book 1", isbn = 123, author = mockAuthor)
        `when`(retrofitService.getDetailedBookById(1)).thenReturn(mockDetailedBook)

        // Call the method
        val result = repository.getDetailedBookById(1)

        // Verify the result
        assertEquals(mockDetailedBook, result)
        verify(retrofitService).getDetailedBookById(1)
    }
}