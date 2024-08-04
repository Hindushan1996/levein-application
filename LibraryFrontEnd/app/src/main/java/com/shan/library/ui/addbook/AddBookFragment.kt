package com.shan.library.ui.addbook

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.shan.library.R
import com.shan.library.databinding.FragmentAddBookBinding

// AddBookFragment.kt
class AddBookFragment : Fragment() {
    private var _binding: FragmentAddBookBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AddBookViewModel by viewModels()

    companion object {
        const val TAG = "AddBookFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddBookBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this@AddBookFragment
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        // Fetch authors and populate the spinner
        viewModel.authors.observe(viewLifecycleOwner) { authors ->
            val authorNames = authors.map { "${it.firstName} ${it.lastName}" }
            val adapter =
                ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, authorNames)
            binding.authorSpinner.adapter = adapter
        }

        // Handle save button click
        binding.saveButton.setOnClickListener {
            val bookName = binding.bookNameEditText.text.toString()
            val isbn = binding.isbnEditText.text.toString().toLongOrNull()
            val selectedPosition = binding.authorSpinner.selectedItemPosition
            val selectedAuthorId = viewModel.authors.value?.get(selectedPosition)?.id

            if (bookName.isEmpty()) { // Check if book name is empty
                binding.bookNameEditText.error = "Book name is required"
            } else if (isbn == null) {
                binding.isbnEditText.error = "Valid ISBN is required"
            } else if (selectedAuthorId == null) {
                Toast.makeText(requireContext(), "Please select an author", Toast.LENGTH_SHORT)
                    .show()
            } else {
                viewModel.addBook(bookName, isbn, selectedAuthorId)
            }
        }

        // Observe book creation
        viewModel.bookCreated.observe(viewLifecycleOwner) { bookCreated ->
            if (bookCreated) {
                Toast.makeText(requireContext(), "Book added successfully", Toast.LENGTH_SHORT)
                    .show()
                findNavController().navigate(R.id.action_addBookFragment_to_homeFragment)
                viewModel.onNavigated()
            }
        }

        // Observe loading state
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        // Observe error
        viewModel.error.observe(viewLifecycleOwner) { error ->
            if (error != null) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}