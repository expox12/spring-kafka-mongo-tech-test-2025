package com.avoris.hotel.controller;

import com.avoris.hotel.dto.SearchCountResponse;
import com.avoris.hotel.dto.SearchIdResponse;
import com.avoris.hotel.dto.SearchRequest;
import com.avoris.hotel.dto.SearchResponse;
import com.avoris.hotel.services.SearchCountService;
import com.avoris.hotel.services.SearchService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SearchController.class)
public class SearchControllerMvcTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private SearchService searchService;

    @MockitoBean
    private SearchCountService searchCountService;

    @Test
    void testValidSearchRequest() throws Exception {
        SearchRequest req = new SearchRequest(
                "H100",
                "24/11/2025",
                "30/12/2025",
                List.of(30, 29, 1, 3)
        );

        when(searchService.createSearch(req)).thenReturn(new SearchIdResponse("abc-123"));
        mockMvc.perform(post("/api/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.searchId").value("abc-123"));
    }

    @Test
    void testCheckInAfterCheckOut() throws Exception {
        SearchRequest req = new SearchRequest(
                "H100",
                "01/12/2025",
                "30/11/2025",
                List.of(30, 29, 1, 3)
        );

        mockMvc.perform(post("/api/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$.errors[0].defaultMessage")
                        .value("checkIn (01/12/2025) must be before checkOut (30/11/2025)"));
    }

    @Test
    void testInvalidDateFormat() throws Exception {
        SearchRequest req = new SearchRequest(
                "H100",
                "2025-12-24",
                "30/11/2025",
                List.of(30, 29, 1, 3)
        );

        mockMvc.perform(post("/api/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].defaultMessage")
                        .value("The date must be in the following format: dd/MM/yyyy"));
    }

    @Test
    void testEmptyAges() throws Exception {
        SearchRequest req = new SearchRequest(
                "H100",
                "24/11/2025",
                "30/11/2025",
                List.of()
        );

        mockMvc.perform(post("/api/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].defaultMessage").value("Cannot be empty"));
    }

    @Test
    void testNegativeAge() throws Exception {
        SearchRequest req = new SearchRequest(
                "H100",
                "24/11/2025",
                "30/11/2025",
                List.of(30, -1, 5)
        );

        mockMvc.perform(post("/api/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].defaultMessage")
                        .value("must be greater than or equal to 0"));
    }

    @Test
    void shouldVerifyCallSearchCountService() throws Exception {
        SearchRequest req = new SearchRequest(
                "H100",
                "24/11/2025",
                "30/12/2025",
                List.of(30, 29, 1, 3)
        );

        when(searchService.createSearch(req)).thenReturn(new SearchIdResponse("ABC123"));

        mockMvc.perform(post("/api/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk());

        verify(searchService).createSearch(eq(req));
    }

    @Test
    void search_shouldReturnSearchCountResponse() throws Exception {
        final String searchId = "ABC123";

        SearchCountResponse mockResponse = new SearchCountResponse(
                searchId,
                new SearchResponse(
                        "H100",
                        LocalDate.of(2025,11,10),
                        LocalDate.of(2025,11,15),
                        List.of(5,7)),
                2
        );

        when(searchCountService.countSimilar(eq(searchId))).thenReturn(mockResponse);

        mockMvc.perform(get("/api/count")
                        .param("searchId", searchId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.searchId").value(searchId))
                .andExpect(jsonPath("$.count").value(2))
                .andExpect(jsonPath("$.search.hotelId").value("H100"))
                .andExpect(jsonPath("$.search.ages[0]").value(5))
                .andExpect(jsonPath("$.search.ages[1]").value(7));

        verify(searchCountService).countSimilar(eq(searchId));
    }
}

