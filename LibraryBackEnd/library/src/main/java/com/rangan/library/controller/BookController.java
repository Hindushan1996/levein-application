package com.rangan.library.controller;

import com.rangan.library.model.Author;
import com.rangan.library.model.Book;
import com.rangan.library.repo.BookRepo;
import com.rangan.library.repo.AuthorRepo;
import com.rangan.library.dto.BookDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookRepo repo;

    @Autowired
    private AuthorRepo authorRepo;

    @PostMapping("/addBook")
    public ResponseEntity<String> addBook(@RequestBody BookDTO bookDTO) {
        try {
            if (bookDTO.getAuthorId() == null) {
                return new ResponseEntity<>("Author ID must not be null", HttpStatus.BAD_REQUEST);
            }

            Optional<Author> authorOptional = authorRepo.findById(bookDTO.getAuthorId());
            if (!authorOptional.isPresent()) {
                return new ResponseEntity<>("Author not found", HttpStatus.NOT_FOUND);
            }

            Author author = authorOptional.get();
            Book book = new Book();
            book.setBookName(bookDTO.getBookName());
            book.setIsbn(bookDTO.getIsbn());
            book.setAuthor(author);  // Correctly setting the Author object

            repo.save(book);

            return new ResponseEntity<>("Book added successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error adding book: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getAllBooks")
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        try {
            List<Book> books = repo.findAll();
            List<BookDTO> bookDTOs = books.stream()
                    .map(book -> new BookDTO(
                            book.getId(),
                            book.getBookName(),
                            book.getIsbn(),
                            book.getAuthor() != null ? book.getAuthor().getId() : null
                    ))
                    .collect(Collectors.toList());
            return new ResponseEntity<>(bookDTOs, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getBook/{id}")
    public ResponseEntity<?> getBookById(@PathVariable Long id) {
        try {
            Optional<Book> book = repo.findById(id);
            if (book.isPresent()) {
                return new ResponseEntity<>(book.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>("No Book found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error fetching book: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/updateBook/{id}")
    public ResponseEntity<String> updateBook(@PathVariable Long id, @RequestBody BookDTO updatedBookDTO) {
        try {
            Optional<Book> existingBookOptional = repo.findById(id);
            if (existingBookOptional.isPresent()) {
                Book existingBook = existingBookOptional.get();

                if (updatedBookDTO.getBookName() != null) {
                    existingBook.setBookName(updatedBookDTO.getBookName());
                }
                if (updatedBookDTO.getIsbn() != null) {
                    existingBook.setIsbn(updatedBookDTO.getIsbn());
                }
                if (updatedBookDTO.getAuthorId() != null) {
                    Optional<Author> authorOptional = authorRepo.findById(updatedBookDTO.getAuthorId());
                    if (authorOptional.isPresent()) {
                        existingBook.setAuthor(authorOptional.get());
                    } else {
                        return new ResponseEntity<>("Author not found", HttpStatus.NOT_FOUND);
                    }
                }

                repo.save(existingBook);
                return new ResponseEntity<>("Book updated successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("No Book found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error updating book: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
