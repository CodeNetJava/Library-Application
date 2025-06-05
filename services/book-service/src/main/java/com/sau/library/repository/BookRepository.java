package com.sau.library.repository;

import com.sau.library.dto.BookRequest;
import com.sau.library.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

    Book findByisbn(String isbn);

    List<Book> findBybookNameContainingIgnoreCase(String name);

    List<Book> findByauthorContainingIgnoreCase(String author);

    int deleteByisbn(String isbn);
}
