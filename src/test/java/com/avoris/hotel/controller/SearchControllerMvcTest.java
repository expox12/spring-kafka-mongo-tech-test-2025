package com.avoris.hotel.controller;

import com.avoris.hotel.dto.SearchRequest;
import com.avoris.hotel.dto.SearchIdResponse;
import com.avoris.hotel.services.SearchCountService;
import com.avoris.hotel.services.SearchService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
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
                "123ABC",
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
                "123ABC",
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
                        .value("checkIn (01/12/2025) debe ser anterior a checkOut (30/11/2025)"));
    }

    @Test
    void testInvalidDateFormat() throws Exception {
        SearchRequest req = new SearchRequest(
                "123ABC",
                "2025-12-24",
                "30/11/2025",
                List.of(30, 29, 1, 3)
        );

        mockMvc.perform(post("/api/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].defaultMessage")
                        .value("La fecha debe ser dd/MM/yyyy"));
    }

    @Test
    void testEmptyAges() throws Exception {
        SearchRequest req = new SearchRequest(
                "123ABC",
                "24/11/2025",
                "30/11/2025",
                List.of()
        );

        mockMvc.perform(post("/api/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].defaultMessage").value("No puede estar vacío"));
    }

    @Test
    void testNegativeAge() throws Exception {
        SearchRequest req = new SearchRequest(
                "123ABC",
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
}

