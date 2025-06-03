package com.sau.library.repository;

import com.sau.library.dto.BookRequest;
import com.sau.library.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Integer> {

      Book findByisbn(String isbn);
}
