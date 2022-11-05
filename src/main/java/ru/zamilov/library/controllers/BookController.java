package ru.zamilov.library.controllers;

import javafx.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.zamilov.library.dao.BookDAO;
import ru.zamilov.library.models.Book;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookDAO bookDAO;

    public BookController(BookDAO bookDAO) {
        this.bookDAO = bookDAO;
    }

    @GetMapping("/show")
    public ResponseEntity<List<Book>> showAllBooks() {
        return ResponseEntity.ok(bookDAO.selectAllBooks());
    }

    @PostMapping("/add")
    public ResponseEntity<Book> addBook(@RequestBody Book book) {
        return new ResponseEntity<>(bookDAO.insertBook(book), HttpStatus.CREATED);
    }

    @GetMapping("/show-by-author")
    public ResponseEntity<List<Book>> getAllBooksByAuthor() {
        return ResponseEntity.ok(bookDAO.selectAllBooksByAuthor());
    }

    @GetMapping("/show-authors")
    public ResponseEntity<List<Pair<String, Integer>>> getAuthors(@RequestParam char symbol) {
        return ResponseEntity.ok(bookDAO.selectAuthorsByParam(symbol));
    }
}
