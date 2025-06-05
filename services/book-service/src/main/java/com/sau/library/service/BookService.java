package com.sau.library.service;

import com.sau.library.dto.BookGetRequest;
import com.sau.library.dto.BookRequest;
import com.sau.library.dto.BookResponse;
import com.sau.library.entity.Book;
import com.sau.library.mappings.Mapper;
import com.sau.library.repository.BookRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BookService {

    private final BookRepository bookRepository;

    // add book
    public String addBook(BookRequest bookRequest) {
        String isbn = bookRequest.isbn() != null && !bookRequest.isbn().isEmpty()
                ? bookRequest.isbn() : this.generateIsbn(bookRequest);
        Book book = bookRepository.findByisbn(isbn);

        if (book != null) {
            book.setTotalCopies(book.getTotalCopies() + bookRequest.copiesCount());
            book.setAvailableCopies(book.getAvailableCopies() + bookRequest.copiesCount());
            bookRepository.save(book);
            return "book is already available updated the stock";
        } else {

            book = Mapper.toBook(bookRequest);
            book.setIsbn(isbn);
            bookRepository.save(book);
            return "book is added in the library";
        }
    }

    private String generateIsbn(BookRequest bookRequest) {
        String bookName = bookRequest.name().toUpperCase();
        String authorName = bookRequest.author().toUpperCase();

        String s1 = bookName.chars().sorted().mapToObj(ch -> String.valueOf(ch)).limit(3).collect(Collectors.joining(""));
        String s2 = authorName.chars().sorted().mapToObj(ch -> String.valueOf(ch)).limit(3).collect(Collectors.joining(""));
        String i = "" + bookName.length() + authorName.length();

        return s1 + s2 + i;
    }

    // get book

    public List<BookResponse> getBook(BookGetRequest bookGetRequest) {
        String isbn = bookGetRequest.isbn();
        String name = bookGetRequest.name();
        String author = bookGetRequest.author();
        List<Book> books = new ArrayList<>();
        if (isbn != null && !isbn.isEmpty()) {
            Book book = bookRepository.findByisbn(isbn);
            books.add(book);
            return books.stream().map(e -> Mapper.toBookResponse(e)).toList();
        } else {
            if (name != null && !name.isEmpty() || author != null && !author.isEmpty()) {

                if (name != null && !name.isEmpty() && author != null && !author.isEmpty()) {
                    books = bookRepository.findBybookNameContainingIgnoreCase(name);
                    books = books.stream().filter(e -> e.getAuthor().toLowerCase().contains(bookGetRequest.author())).toList();
                } else if (name != null && !name.isEmpty() && (author == null || author.isEmpty())) {
                    books = bookRepository.findBybookNameContainingIgnoreCase(name);
                } else if (author != null && !author.isEmpty() && (name == null || name.isEmpty())) {
                    books = bookRepository.findByauthorContainingIgnoreCase(author);
                }
                return books.stream().map(e -> Mapper.toBookResponse(e)).toList();
            }
            return books.stream().map(e -> Mapper.toBookResponse(e)).toList();
        }
    }

    // remove book
    @Transactional
    public String removebook(String isbn) {
        int n = bookRepository.deleteByisbn(isbn);
        if (n > 0) {
            return "book has been deleted from the library";
        } else {
            return "book not available in the library";
        }
    }

}