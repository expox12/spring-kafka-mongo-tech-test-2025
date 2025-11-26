package com.avoris.hotel.services;

import com.avoris.hotel.dto.SearchCountResponse;
import com.avoris.hotel.dto.SearchResponse;
import com.avoris.hotel.models.SearchEntity;
import com.avoris.hotel.models.SearchNotFoundException;
import com.avoris.hotel.repository.SearchRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SearchCountServiceTest {

    private SearchRepository searchRepository;
    private SearchCountService searchCountService;

    @BeforeEach
    void setup() {
        searchRepository = Mockito.mock(SearchRepository.class);
        searchCountService = new SearchCountService(searchRepository);
    }

    @Test
    void shouldReturnSearchCountResponseCorrectly() {
        String searchId = "ABC123";

        SearchEntity entity = new SearchEntity(
                searchId,
                "H100",
                LocalDate.of(2025, 11, 10),
                LocalDate.of(2025, 11, 15),
                List.of(5, 7)
        );

        when(searchRepository.findById(searchId)).thenReturn(Optional.of(entity));
        when(searchRepository.countSimilar(any(), any(), any(), any(), any(), anyList(), anyInt()))
                .thenReturn(2L);

        SearchCountResponse response = searchCountService.countSimilar(searchId);

        assertThat(response.searchId()).isEqualTo(searchId);
        assertThat(response.count()).isEqualTo(2);

        SearchResponse sr = response.search();
        assertThat(sr.hotelId()).isEqualTo("H100");
        assertThat(sr.checkIn()).isEqualTo(LocalDate.of(2025, 11, 10));
        assertThat(sr.checkOut()).isEqualTo(LocalDate.of(2025, 11, 15));
        assertThat(sr.ages()).containsExactly(5, 7);

        ArgumentCaptor<LocalDate> inGte = ArgumentCaptor.forClass(LocalDate.class);
        ArgumentCaptor<LocalDate> inLte = ArgumentCaptor.forClass(LocalDate.class);
        ArgumentCaptor<LocalDate> outGte = ArgumentCaptor.forClass(LocalDate.class);
        ArgumentCaptor<LocalDate> outLte = ArgumentCaptor.forClass(LocalDate.class);

        verify(searchRepository).countSimilar(
                eq("H100"),
                inGte.capture(),
                inLte.capture(),
                outGte.capture(),
                outLte.capture(),
                eq(List.of(5, 7)),
                eq(2)
        );

        assertThat(inGte.getValue()).isEqualTo(LocalDate.of(2025, 11, 9));
        assertThat(inLte.getValue()).isEqualTo(LocalDate.of(2025, 11, 11));
        assertThat(outGte.getValue()).isEqualTo(LocalDate.of(2025, 11, 14));
        assertThat(outLte.getValue()).isEqualTo(LocalDate.of(2025, 11, 16));
    }

    @Test
    void shouldThrowExceptionWhenNotFound() {
        when(searchRepository.findById("ABC123")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> searchCountService.countSimilar("ABC123"))
                .isInstanceOf(SearchNotFoundException.class)
                .hasMessageContaining("ABC123");
    }

}