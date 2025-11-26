package com.avoris.hotel.services;

import com.avoris.hotel.dto.SearchIdResponse;
import com.avoris.hotel.dto.SearchMessage;
import com.avoris.hotel.dto.SearchRequest;
import com.avoris.hotel.kafka.SearchProducer;
import com.avoris.hotel.mapper.SearchMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SearchServiceTest {

    private SearchProducer kafkaTemplate;
    private SearchMapper mapper;

    private SearchService searchService;

    @BeforeEach
    void setup() {
        kafkaTemplate = Mockito.mock(SearchProducer.class);
        mapper = Mockito.mock(SearchMapper.class);

        searchService = new SearchService(kafkaTemplate, mapper);
    }

    @Test
    void createSearch_shouldPublishMessageAndReturnSearchId() {
        SearchRequest request = new SearchRequest(
                "H100",
                "10/11/2025",
                "15/11/2025",
                List.of(5, 7)
        );

        SearchMessage mappedMessage = new SearchMessage(
                "ABC123",
                "H100",
                LocalDate.of(2025, 11, 10),
                LocalDate.of(2025, 11, 15),
                List.of(5, 7)
        );

        when(mapper.toSearchMessage(anyString(), eq(request))).thenReturn(mappedMessage);

        SearchIdResponse response = searchService.createSearch(request);

        assertThat(response).isNotNull();
        assertThat(response.searchId()).isNotNull();

        ArgumentCaptor<String> searchIdCaptor = ArgumentCaptor.forClass(String.class);
        verify(mapper).toSearchMessage(searchIdCaptor.capture(), eq(request));

        assertThat(searchIdCaptor.getValue()).isEqualTo(response.searchId());

        verify(kafkaTemplate).publishMessage(mappedMessage);
    }

}