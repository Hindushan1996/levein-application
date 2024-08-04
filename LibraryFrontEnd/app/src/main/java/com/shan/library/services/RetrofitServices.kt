package com.shan.library.services

import com.shan.library.model.Author
import com.shan.library.model.AuthorUpdateRequest
import com.shan.library.model.CreateAuthor
import com.shan.library.model.Book
import com.shan.library.model.BookUpdateRequest
import com.shan.library.model.CreateBook
import com.shan.library.model.DetailedBook
import lk.vci.venturacollector.common.Constant
import retrofit2.http.*

interface RetrofitServices {

    @GET(Constant.GET_BOOKS_URL)
    suspend fun getAllBooks(): List<Book>

    @POST(Constant.CREATE_AUTHOR_URL)
    suspend fun createAuthor(@Body createAuthor: CreateAuthor)

    @GET(Constant.GET_AUTHORS_URL)
    suspend fun getAllAuthors(): List<Author>

    @POST(Constant.ADD_BOOK_URL)
    suspend fun addBook(@Body createBook: CreateBook)

    @GET(Constant.GET_BOOK_BY_ID_URL)
    suspend fun getDetailedBookById(@Path("id") bookId: Int): DetailedBook

    @PUT(Constant.UPDATE_AUTHOR_URL)
    suspend fun updateAuthor(
        @Path("id") authorId: Int,
        @Body authorUpdateRequest: AuthorUpdateRequest
    )

    @PUT(Constant.UPDATE_BOOK_URL)
    suspend fun updateBook(
        @Path("id") bookId: Int,
        @Body bookUpdateRequest: BookUpdateRequest
    )

}