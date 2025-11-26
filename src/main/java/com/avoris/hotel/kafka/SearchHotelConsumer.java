package com.avoris.hotel.kafka;

import com.avoris.hotel.dto.SearchMessage;
import com.avoris.hotel.mapper.SearchMapper;
import com.avoris.hotel.models.SearchEntity;
import com.avoris.hotel.repository.SearchRepository;
import com.avoris.hotel.util.Constants;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class SearchHotelConsumer {

    private final SearchRepository searchRepository;
    private final SearchMapper searchMapper;

    public SearchHotelConsumer(SearchRepository searchRepository, SearchMapper searchMapper) {
        this.searchRepository = searchRepository;
        this.searchMapper = searchMapper;
    }

    @KafkaListener(topics = Constants.HOTEL_SEARCH_TOPIC, groupId = Constants.AVAILABILITY_SEARCH_GROUP)
    public void consumeHotelSearchConfirmation(SearchMessage searchMessage) {
        System.out.println("--+++++ CONSUMER: test entity");
        SearchEntity searchEntity = searchMapper.toEntity(searchMessage);
        System.out.println(searchEntity);
        searchRepository.save(searchEntity);
    }
}
