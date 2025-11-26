package com.avoris.hotel.repository;

import com.avoris.hotel.models.SearchEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface SearchRepository extends MongoRepository<SearchEntity, String> {

    @Query(value = """
    {
        "hotelId": ?0,
        "checkIn": { "$gte": ?1, "$lte": ?2 },
        "checkOut": { "$gte": ?3, "$lte": ?4 },
        "ages": { $all: ?5, $size: ?6 }
    }
    """, count = true)
    long countSimilar(
            String hotelId,
            LocalDate checkInGte,
            LocalDate checkInLte,
            LocalDate checkOutGte,
            LocalDate checkOutLte,
            List<Integer> ages,
            int size
    );
}
