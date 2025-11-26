package com.avoris.hotel.dto;

public record SearchCountResponse(String searchId, SearchResponse search, long count) {
}
