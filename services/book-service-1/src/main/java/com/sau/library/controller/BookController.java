//package com.sau.library.controller;
//
//import com.sau.library.dto.BookRequest;
//import com.sau.library.service.BookService;
//import lombok.AllArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@AllArgsConstructor
//@RestController
//@RequestMapping("/book")
//public class BookController {
//
//    private final BookService bookService;
//
//    @PutMapping("/addBook")
//    public ResponseEntity<String> addBook(@RequestBody BookRequest bookRequest){
//       return ResponseEntity.ok(bookService.addBook(bookRequest));
//    }
//}
