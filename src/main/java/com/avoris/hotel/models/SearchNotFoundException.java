package com.avoris.hotel.models;

public class SearchNotFoundException extends RuntimeException {
    public SearchNotFoundException(String searchId) {
        super("Search with id %s not found".formatted(searchId));
    }
}
