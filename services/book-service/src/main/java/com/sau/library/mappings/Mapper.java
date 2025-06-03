package com.sau.library.mappings;

import com.sau.library.dto.BookRequest;
import com.sau.library.entity.Book;

public class Mapper {
    public static Book toBook(BookRequest bookRequest){
        return Book.builder()
                .bookName(bookRequest.name())
                .author(bookRequest.author())
                .totalCopies(bookRequest.copiesCount())
                .availableCopies(bookRequest.copiesCount())
                .isbn(bookRequest.isbn())
                .build();
    }
}
