package org.example.trainsystem.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.trainsystem.dto.BookingRequestDTO;
import org.example.trainsystem.dto.RouteResponseDTO;
import org.example.trainsystem.models.Booking;
import org.example.trainsystem.services.BookingService;
import org.example.trainsystem.services.RouteService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PassengerController.class)
public class PassengerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RouteService routeService;

    @MockBean
    private BookingService bookingService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void searchRoutes_Success() throws Exception {
        RouteResponseDTO response = new RouteResponseDTO(1L, "T100", "A", "B", "10:00", "12:00");

        Mockito.when(routeService.findRoutes("A", "B")).thenReturn(Arrays.asList(response));

        mockMvc.perform(get("/api/passenger/search")
                        .param("from", "A")
                        .param("to", "B"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].trainId").value(1))
                .andExpect(jsonPath("$[0].trainNumber").value("T100"));
    }

    @Test
    void bookTicket_Success() throws Exception {
        BookingRequestDTO request = new BookingRequestDTO();
        // Set properties if needed, depending on validation rules (mock will ignore anyway unless validation fails)

        Booking booking = new Booking();
        booking.setId(1L);

        Mockito.when(bookingService.createBooking(any(BookingRequestDTO.class))).thenReturn(booking);

        mockMvc.perform(post("/api/passenger/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }
}
