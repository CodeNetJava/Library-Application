//package com.sau.library.service;
//import com.sau.library.dto.BookRequest;
//import com.sau.library.entity.Book;
//import com.sau.library.mappings.Mapper;
//import com.sau.library.repository.BookRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import java.util.stream.Collectors;
//
//@RequiredArgsConstructor
//@Service
//public class BookService {
//
//    // add the book/ use the put method
//    //check if book present the only increment quantity
//    // if not start from quntity as 1
//    private final BookRepository bookRepository;
//    public String addBook(BookRequest bookRequest) {
//        String isbn = bookRequest.isbn() != null && !bookRequest.isbn().isEmpty()
//                ? bookRequest.isbn() : this.generateIsbn(bookRequest);
//        Book book = bookRepository.findByisbn(isbn);
//
//        if(book != null)
//        {
//            book.setTotalCopies(book.getTotalCopies()+bookRequest.copiesCount());
//            book.setAvailableCopies(book.getAvailableCopies() + bookRequest.copiesCount());
//            bookRepository.save(book);
//            return "book is already available updated the stock";
//        }
//        else{
//            bookRepository.save(Mapper.toBook(bookRequest));
//            return "book is added in the library";
//        }
//    }
//
//    private String generateIsbn(BookRequest bookRequest) {
//        String bookName = bookRequest.name().toUpperCase();
//        String authorName = bookRequest.author().toUpperCase();
//
//        String s1  = bookName.chars().sorted().mapToObj( ch -> String.valueOf(ch)).limit(3).collect(Collectors.joining(""));
//
//        String s2  = authorName.chars().sorted().mapToObj(ch -> String.valueOf(ch)).limit(3).collect(Collectors.joining(""));
//        String i = ""+ bookName.length() + authorName.length();
//
//        return s1 + s2 + i;
//    }
//}
