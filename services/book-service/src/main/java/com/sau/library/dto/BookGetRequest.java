package com.sau.library.dto;

public record BookGetRequest(
        String name,
        String author,
        String isbn
) {
}
