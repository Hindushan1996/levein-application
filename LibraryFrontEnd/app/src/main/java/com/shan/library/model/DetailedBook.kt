package com.shan.library.model

import com.squareup.moshi.Json

data class DetailedBook(
    @Json(name = "id") val id: Int,
    @Json(name = "bookName") val bookName: String,
    @Json(name = "isbn") val isbn: Long,
    @Json(name = "author") val author: Author
)