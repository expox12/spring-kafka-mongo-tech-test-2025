package com.avoris.hotel.services;

import com.avoris.hotel.dto.SearchCountResponse;
import com.avoris.hotel.dto.SearchResponse;
import com.avoris.hotel.models.SearchEntity;
import com.avoris.hotel.models.SearchNotFoundException;
import com.avoris.hotel.repository.SearchRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class SearchCountService {

    private final SearchRepository searchRepository;

    public SearchCountService(SearchRepository searchRepository) {
        this.searchRepository = searchRepository;
    }

    public SearchCountResponse countSimilar(String searchId) {
        SearchEntity searchEntity = searchRepository.findById(searchId)
                .orElseThrow(() -> new SearchNotFoundException(searchId));

        List<Integer> ages = searchEntity.getAges();

        LocalDate checkInMinus1 = searchEntity.getCheckIn().minusDays(1);
        LocalDate checkInPlus1 = searchEntity.getCheckIn().plusDays(1);
        LocalDate checkOutMinus1 = searchEntity.getCheckOut().minusDays(1);
        LocalDate checkOutPlus1 = searchEntity.getCheckOut().plusDays(1);

        long count = searchRepository.countSimilar(
                searchEntity.getHotelId(),
                checkInMinus1,
                checkInPlus1,
                checkOutMinus1,
                checkOutPlus1,
                ages,
                ages.size()
        );

        return new SearchCountResponse(
                searchId,
                new SearchResponse(
                    searchEntity.getHotelId(),
                    searchEntity.getCheckIn(),
                    searchEntity.getCheckOut(),
                    searchEntity.getAges()
                ),
                count
        );
    }
}
