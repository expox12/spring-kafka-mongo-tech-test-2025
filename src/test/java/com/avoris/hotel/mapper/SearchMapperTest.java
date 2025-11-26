package com.avoris.hotel.mapper;

import com.avoris.hotel.dto.SearchMessage;
import com.avoris.hotel.dto.SearchRequest;
import com.avoris.hotel.models.SearchEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SearchMapperTest {

    private final SearchMapper mapper = new SearchMapper();

    @Test
    void testSearchMessageToSearchEntity() {
        SearchMessage message = new SearchMessage(
                "ABC123",
                "H100",
                LocalDate.of(2025, 11, 10),
                LocalDate.of(2025, 11, 15),
                List.of(5, 7)
        );

        SearchEntity entity = mapper.toEntity(message);

        assertThat(entity.getSearchId()).isEqualTo("ABC123");
        assertThat(entity.getHotelId()).isEqualTo("H100");
        assertThat(entity.getCheckIn()).isEqualTo(LocalDate.of(2025, 11, 10));
        assertThat(entity.getCheckOut()).isEqualTo(LocalDate.of(2025, 11, 15));
        assertThat(entity.getAges()).containsExactly(5, 7);
    }

    @Test
    void testSearchEntityToSearchMessage() {
        SearchRequest request = new SearchRequest(
                "H300",
                "10/11/2025",
                "15/11/2025",
                List.of(5, 7)
        );

        SearchMessage message = mapper.toSearchMessage("ABC123", request);

        assertThat(message.searchId()).isEqualTo("ABC123");
        assertThat(message.hotelId()).isEqualTo("H300");
        assertThat(message.checkIn()).isEqualTo(LocalDate.of(2025, 11, 10));
        assertThat(message.checkOut()).isEqualTo(LocalDate.of(2025, 11, 15));
        assertThat(message.ages()).containsExactly(5, 7);
    }
}