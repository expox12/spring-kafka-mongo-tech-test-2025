package com.avoris.hotel.controller;

import com.avoris.hotel.dto.SearchCountResponse;
import com.avoris.hotel.dto.SearchIdResponse;
import com.avoris.hotel.dto.SearchRequest;
import com.avoris.hotel.services.SearchCountService;
import com.avoris.hotel.services.SearchService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class SearchController {

    private final SearchService searchService;
    private final SearchCountService searchCountService;

    public SearchController(SearchService searchService, SearchCountService searchCountService) {
        this.searchService = searchService;
        this.searchCountService = searchCountService;
    }

    @PostMapping("/search")
    public ResponseEntity<SearchIdResponse> search(@Valid @RequestBody SearchRequest request) {
        SearchIdResponse response = searchService.createSearch(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/count")
    public ResponseEntity<SearchCountResponse> search(@RequestParam String searchId) {
        SearchCountResponse response = searchCountService.countSimilar(searchId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
