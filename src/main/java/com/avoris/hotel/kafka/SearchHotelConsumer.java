package com.avoris.hotel.kafka;

import com.avoris.hotel.dto.SearchMessage;
import com.avoris.hotel.mapper.SearchMapper;
import com.avoris.hotel.models.SearchEntity;
import com.avoris.hotel.repository.SearchRepository;
import com.avoris.hotel.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class SearchHotelConsumer {
    private static final Logger log = LoggerFactory.getLogger(SearchHotelConsumer.class);

    private final SearchRepository searchRepository;
    private final SearchMapper searchMapper;

    public SearchHotelConsumer(SearchRepository searchRepository, SearchMapper searchMapper) {
        this.searchRepository = searchRepository;
        this.searchMapper = searchMapper;
    }

    @KafkaListener(topics = Constants.HOTEL_SEARCH_TOPIC, groupId = Constants.AVAILABILITY_SEARCH_GROUP)
    public void consumeHotelSearchConfirmation(SearchMessage searchMessage) {
        log.info("Message received from topic {} and groupId: {}", Constants.HOTEL_SEARCH_TOPIC, Constants.AVAILABILITY_SEARCH_GROUP);
        SearchEntity searchEntity = searchMapper.toEntity(searchMessage);
        searchRepository.save(searchEntity);
    }
}
