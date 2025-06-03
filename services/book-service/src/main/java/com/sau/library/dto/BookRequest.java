package com.sau.library.dto;

public record BookRequest(
        String name,
        String author,
        String isbn,
        int copiesCount
) {
}
