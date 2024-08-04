package com.shan.library.ui.home

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.shan.library.adapter.BookAdapter
import com.shan.library.databinding.FragmentBookListBinding

class BookListFragment : Fragment() {

    private var _binding: FragmentBookListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BookListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookListBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeViewModel()
    }

    /**
     * Sets up the RecyclerView for displaying the list of books.
     */
    private fun setupRecyclerView() {
        val bookAdapter = BookAdapter { book ->
            // Navigate to BookDetailFragment with the selected book ID
            val action = BookListFragmentDirections.actionBookListFragmentToBookDetailFragment(book.id)
            findNavController().navigate(action)
        }
        binding.bookListRecyclerview.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = bookAdapter
        }
    }

    /**
     * Observes the ViewModel's LiveData to update the UI and handle data changes.
     */
    private fun observeViewModel() {
        // Observe the books LiveData from the ViewModel
        viewModel.books.observe(viewLifecycleOwner) { books ->
            books?.let {
                // Submit the list of books to the adapter
                (binding.bookListRecyclerview.adapter as BookAdapter).submitList(it)
            }
        }

        // Observe the isLoading LiveData to show or hide the progress bar
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        // Observe the error LiveData to show a toast message if an error occurs
        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}