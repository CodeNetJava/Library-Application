package com.sau.library.dto;

public record BookResponse(
        String name,
        String author,
        String isbn,
        int availableCopies
) {
}
