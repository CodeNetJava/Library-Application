package com.sau.library.entity;

import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@Entity
public class Book {
    private int id;
    private String bookName;
    private String author;
    private int totalCopies;
    private String isbn;
    private int availableCopies;
}
