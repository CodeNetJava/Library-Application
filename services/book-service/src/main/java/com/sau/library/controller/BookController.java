package com.sau.library.controller;
import com.sau.library.dto.BookGetRequest;
import com.sau.library.dto.BookRequest;
import com.sau.library.dto.BookResponse;
import com.sau.library.service.BookService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/book")
public class BookController {

    private final BookService bookService;

   @PutMapping("/addBook")
    public ResponseEntity<String> addBook(@RequestBody BookRequest bookRequest,@RequestHeader("X-Username") String usernmae ){
       return ResponseEntity.ok(bookService.addBook(bookRequest,usernmae));
    }
    // get book

    @GetMapping("/getbook")
    public ResponseEntity<List<BookResponse>> getBook(@RequestBody BookGetRequest bookGetRequest){
       return ResponseEntity.ok(bookService.getBook(bookGetRequest));
    }
    // remove book

    @DeleteMapping("/deletebook/{isbn}")
    public ResponseEntity<String> deletebook(@PathVariable String isbn){
       return ResponseEntity.ok(bookService.removebook(isbn));
    }
    // update stock
}
