package com.shan.library.ui.bookdetail

import android.app.AlertDialog
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import com.shan.library.R
import com.shan.library.databinding.FragmentBookDetailBinding

class BookDetailFragment  : Fragment() {

    private var _binding: FragmentBookDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BookDetailViewModel by viewModels()
    private val bookId: Int by lazy {
        arguments?.getInt("bookId") ?: 0
    }

    companion object { // Factory method to create a new instance of the fragment
        fun newInstance(bookId: Int): BookDetailFragment {
            return BookDetailFragment().apply {
                arguments = Bundle().apply {
                    putInt("bookId", bookId)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookDetailBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this@BookDetailFragment
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.fetchBookDetails(bookId)

        init()
    }

    private fun init() {
        binding.editBookButton.setOnClickListener { // Handle edit book button click
            showEditBookDialog()
        }

        // Handle edit author button click
        binding.editAuthorButton.setOnClickListener {
            showEditAuthorDialog(viewModel.book.value?.author?.id ?: 0)
        }

        // Observe the book LiveData
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        // Observe the error LiveData
        viewModel.error.observe(viewLifecycleOwner) { error ->
            if (error != null) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
            }
        }    }

    /**
     * Show a dialog to edit book details.
     */
    private fun showEditBookDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_edit_book, null)
        val bookNameEditText = dialogView.findViewById<EditText>(R.id.bookNameEditText)
        val isbnEditText = dialogView.findViewById<EditText>(R.id.isbnEditText)

        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Edit Book")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                val bookName = bookNameEditText.text.toString()
                val isbn = isbnEditText.text.toString().toIntOrNull() ?: 0
                if (bookName.isNotEmpty() && isbn != 0) {
                    viewModel.updateBook(bookId, bookName, isbn)
                } else {
                    Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .create()

        dialog.show()
    }

    /**
     * Show a dialog to edit author details.
     */
    private fun showEditAuthorDialog(authorId: Int) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_edit_author, null)
        val firstNameEditText = dialogView.findViewById<EditText>(R.id.firstNameEditText)
        val lastNameEditText = dialogView.findViewById<EditText>(R.id.lastNameEditText)

        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Edit Author")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                val firstName = firstNameEditText.text.toString()
                val lastName = lastNameEditText.text.toString()
                if (firstName.isNotEmpty() && lastName.isNotEmpty()) {
                    viewModel.updateAuthor(bookId,authorId, firstName, lastName)
                } else {
                    Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .create()

        dialog.show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}