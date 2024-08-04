package com.shan.library.adapter

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.shan.library.model.Book

// BindingAdapters.kt
@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<Book>?) {
    val adapter = recyclerView.adapter as? BookAdapter
    adapter?.submitList(data)
}