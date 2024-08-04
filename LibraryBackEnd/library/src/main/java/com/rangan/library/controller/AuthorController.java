package com.rangan.library.controller;

import com.rangan.library.model.Author;
import com.rangan.library.repo.AuthorRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/authors")
public class AuthorController {

    @Autowired
    private AuthorRepo authorRepo;

    @GetMapping("/getAllAuthors")
    public ResponseEntity<List<Author>> getAllAuthors() {
        try {
            List<Author> authors = authorRepo.findAll();
            return new ResponseEntity<>(authors, HttpStatus.OK);
        } catch (Exception e) {
            // You may want to log the exception here
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/getAuthor/{id}")
    public ResponseEntity<?> getBookById(@PathVariable Long id) {
        try {
            Optional<Author> author = authorRepo.findById(id);
            if (author.isPresent()) {
                return new ResponseEntity<>(author.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>("No Author found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error fetching book: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/createAuthor")
    public ResponseEntity<String> createAuthor(@RequestBody Author author) {
        try {
            Author savedAuthor = authorRepo.save(author);
            return new ResponseEntity<>("Author created successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            // Optionally log the exception
            return new ResponseEntity<>("Error creating author: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/updateAuthor/{id}")
    public ResponseEntity<String> updateAuthor(@PathVariable Long id, @RequestBody Author updatedAuthor) {
        try {
            Optional<Author> existingAuthorOptional = authorRepo.findById(id);
            if (existingAuthorOptional.isPresent()) {
                Author existingAuthor = existingAuthorOptional.get();

                // Update only the fields that are not null
                if (updatedAuthor.getFirstName() != null) {
                    existingAuthor.setFirstName(updatedAuthor.getFirstName());
                }
                if (updatedAuthor.getLastName() != null) {
                    existingAuthor.setLastName(updatedAuthor.getLastName());
                }

                authorRepo.save(existingAuthor);
                return new ResponseEntity<>("Author updated successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("No Author found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error updating author: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}
