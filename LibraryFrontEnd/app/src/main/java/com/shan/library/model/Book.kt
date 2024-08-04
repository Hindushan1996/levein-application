package com.shan.library.model

import com.squareup.moshi.Json

data class Book(
    @Json(name = "id") val id: Int,
    @Json(name = "bookName") val bookName: String,
    @Json(name = "isbn") val isbn: Long,
    @Json(name = "authorId") val authorId: Int
)