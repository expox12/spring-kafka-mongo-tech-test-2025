package com.avoris.hotel.mapper;

import com.avoris.hotel.dto.SearchMessage;
import com.avoris.hotel.dto.SearchRequest;
import com.avoris.hotel.models.SearchEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class SearchMapper {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public SearchEntity toEntity(SearchMessage message) {
        return new SearchEntity(
                message.searchId(),
                message.hotelId(),
                message.checkIn(),
                message.checkOut(),
                message.ages()
        );
    }

    public SearchMessage toSearchMessage(String searchId, SearchRequest request) {
        return new SearchMessage(
                searchId,
                request.hotelId(),
                LocalDate.parse(request.checkIn(), DATE_FORMAT),
                LocalDate.parse(request.checkOut(), DATE_FORMAT),
                request.ages()
        );
    }
}
