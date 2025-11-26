package com.avoris.hotel.models;

public class SearchNotFoundException extends RuntimeException {
    public SearchNotFoundException(String searchId) {
        super("Busqueda con id %s no encontrada".formatted(searchId));
    }
}
