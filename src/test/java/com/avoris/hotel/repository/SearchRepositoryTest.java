package com.avoris.hotel.repository;

import com.avoris.hotel.models.SearchEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.mongodb.test.autoconfigure.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@ActiveProfiles("test")
public class SearchRepositoryTest {

    @Autowired
    private SearchRepository searchRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    private static final String HOTEL_TEST_DB = "hotel_test";

    @BeforeEach
    void setup() {
        mongoTemplate.dropCollection(HOTEL_TEST_DB);

        mongoTemplate.save(new SearchEntity(
                "1", "H100",
                LocalDate.of(2025, 11, 10),
                LocalDate.of(2025, 11, 15),
                List.of(29, 32)
        ));

        mongoTemplate.save(new SearchEntity(
                "2", "H100",
                LocalDate.of(2025, 11, 11),
                LocalDate.of(2025, 11, 14),
                List.of(32, 29)
        ));

        mongoTemplate.save(new SearchEntity(
                "3", "H200",
                LocalDate.of(2025, 11, 10),
                LocalDate.of(2025, 11, 15),
                List.of(29, 32)
        ));

        mongoTemplate.save(new SearchEntity(
                "4", "H100",
                LocalDate.of(2025, 11, 20),
                LocalDate.of(2025, 11, 25),
                List.of(29, 32)
        ));
    }

    @AfterEach
    void cleanUpEach() {
        mongoTemplate.dropCollection(HOTEL_TEST_DB);
    }

    @Test
    void shouldCountSimilarWithExactAndPlusMinusOneDay() {
        String hotelId = "H100";

        LocalDate checkInTarget = LocalDate.of(2025, 11, 10);
        LocalDate checkOutTarget = LocalDate.of(2025, 11, 15);

        LocalDate checkInGte = checkInTarget.minusDays(1);
        LocalDate checkInLte = checkInTarget.plusDays(1);

        LocalDate checkOutGte = checkOutTarget.minusDays(1);
        LocalDate checkOutLte = checkOutTarget.plusDays(1);

        List<Integer> ages = List.of(29, 32);
        int size = ages.size();

        long count = searchRepository.countSimilar(
                hotelId,
                checkInGte, checkInLte,
                checkOutGte, checkOutLte,
                ages,
                size
        );

        assertThat(count).isEqualTo(2);
    }
}
