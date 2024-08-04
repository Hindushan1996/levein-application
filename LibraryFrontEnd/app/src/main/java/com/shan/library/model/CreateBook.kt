package com.shan.library.model

data class CreateBook(
    val bookName: String,
    val isbn: Long,
    val authorId: Int
)
