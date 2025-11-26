package com.avoris.hotel.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Document(collection = "hotel")
public class SearchEntity {

    @Id
    private String searchId;

    private String hotelId;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private List<Integer> ages;

    public SearchEntity() {}

    public SearchEntity(String searchId, String hotelId, LocalDate checkIn, LocalDate checkOut, List<Integer> ages) {
        this.searchId = searchId;
        this.hotelId = hotelId;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.ages = ages.stream().sorted().toList();
    }

    public String getSearchId() {
        return searchId;
    }

    public String getHotelId() {
        return hotelId;
    }

    public LocalDate getCheckIn() {
        return checkIn;
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }

    public List<Integer> getAges() {
        return ages;
    }

    @Override
    public String toString() {
        return "SearchEntity{" +
                "searchId='" + searchId + '\'' +
                ", hotelId='" + hotelId + '\'' +
                ", checkIn=" + checkIn +
                ", checkOut=" + checkOut +
                ", ages=" + ages +
                '}';
    }
}
