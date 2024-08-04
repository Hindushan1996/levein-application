package com.shan.library.ui.addauthor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.shan.library.R
import com.shan.library.databinding.FragmentAddAuthorBinding

class AddAuthorFragment : Fragment() {

    private var _binding: FragmentAddAuthorBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AddAuthorViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddAuthorBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this@AddAuthorFragment
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        binding.signInBtn.setOnClickListener { // Handle sign-in button click
            val firstName = binding.firstName.editText?.text.toString()
            val lastName = binding.lastName.editText?.text.toString()
            if (firstName.isEmpty() || lastName.isEmpty()) {
                if (firstName.isEmpty()) {
                    binding.firstName.error = "First Name is required"
                } else {
                    binding.firstName.error = null
                }
                if (lastName.isEmpty()) {
                    binding.lastName.error = "Last Name is required"
                } else {
                    binding.lastName.error = null
                }
            } else {
                viewModel.createAuthor(firstName, lastName)
            }
        }

        // Observe the authorCreated LiveData
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        // Observe the error LiveData
        viewModel.error.observe(viewLifecycleOwner) { error ->
            if (error != null) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
            }
        }

        // Observe the authorCreated LiveData
        viewModel.authorCreated.observe(viewLifecycleOwner) { isCreated ->
            if (isCreated == true) {
                Toast.makeText(requireContext(), "Author created successfully", Toast.LENGTH_SHORT).show()
                clearForm()
                findNavController().navigate(R.id.action_addAuthor_to_homeFragment)
                viewModel.onNavigated() // Reset the state after navigation
            }
        }
    }

    /**
     * Clears the form fields.
     */
    private fun clearForm() {
        binding.firstName.editText?.text?.clear()
        binding.lastName.editText?.text?.clear()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}