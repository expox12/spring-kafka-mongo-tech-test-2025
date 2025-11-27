package com.avoris.hotel.services;

import com.avoris.hotel.dto.SearchIdResponse;
import com.avoris.hotel.dto.SearchMessage;
import com.avoris.hotel.dto.SearchRequest;
import com.avoris.hotel.kafka.SearchProducer;
import com.avoris.hotel.mapper.SearchMapper;
import com.avoris.hotel.models.ObjectIdGenerator;
import org.springframework.stereotype.Service;

@Service
public class SearchService {

    private final SearchProducer kafkaTemplate;
    private final SearchMapper searchMapper;
    private final ObjectIdGenerator objectIdGenerator;

    public SearchService(SearchProducer kafkaTemplate, SearchMapper searchMapper, ObjectIdGenerator objectIdGenerator) {
        this.kafkaTemplate = kafkaTemplate;
        this.searchMapper = searchMapper;
        this.objectIdGenerator = objectIdGenerator;
    }

    public SearchIdResponse createSearch(SearchRequest request) {
        String searchId = objectIdGenerator.generate();
        SearchMessage searchMessage = searchMapper.toSearchMessage(searchId, request);
        kafkaTemplate.publishMessage(searchMessage);
        return new SearchIdResponse(searchId);
    }
}
