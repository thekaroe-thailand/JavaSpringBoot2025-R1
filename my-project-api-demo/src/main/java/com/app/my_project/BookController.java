package com.app.my_project;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.models.BookModel;

@RestController
@RequestMapping("/api/books")
public class BookController {
    private List<BookModel> books = new ArrayList<>(
        Arrays.asList(
            new BookModel("1", "Book 1", 100),
            new BookModel("2", "Book 2", 200),
            new BookModel("3", "Book 3", 300)
        )
    );

    @GetMapping
    public List<BookModel> getAllBooks() {
        return books;
    }

    @GetMapping("/{id}")
    public BookModel getBookById(@PathVariable String id) {
        return books.stream()
            .filter(book -> book.getId().equals(id))
            .findFirst()
            .orElse(null);
    }

    @PostMapping
    public BookModel addBook(@RequestBody BookModel book) {
        books.add(book);
        return book;
    }

    @PutMapping("/{id}")
    public BookModel updateBook(@PathVariable String id, @RequestBody BookModel updateBook) {
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getId().equals(id)) {
                books.set(i, updateBook);
                return updateBook;
            }
        }

        return null;
    }

    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable String id) {
        books.removeIf(book -> book.getId().equals(id));
    }
}
