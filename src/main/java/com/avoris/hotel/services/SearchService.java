package com.avoris.hotel.services;

import com.avoris.hotel.dto.SearchIdResponse;
import com.avoris.hotel.dto.SearchMessage;
import com.avoris.hotel.dto.SearchRequest;
import com.avoris.hotel.kafka.SearchProducer;
import com.avoris.hotel.mapper.SearchMapper;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SearchService {

    private final SearchProducer kafkaTemplate;
    private final SearchMapper searchMapper;

    public SearchService(SearchProducer kafkaTemplate, SearchMapper searchMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.searchMapper = searchMapper;
    }

    public SearchIdResponse createSearch(SearchRequest request) {
        String searchId = UUID.randomUUID().toString();
        SearchMessage searchMessage = searchMapper.toSearchMessage(searchId, request);
        searchMessage.checkIn().minusDays(1);
        kafkaTemplate.publishMessage(searchMessage);
        return new SearchIdResponse(searchId);
    }
}
