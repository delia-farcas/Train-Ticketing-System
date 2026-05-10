package org.example.trainsystem.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.trainsystem.dto.AdminBookingDTO;
import org.example.trainsystem.dto.RouteRequestDTO;
import org.example.trainsystem.models.Booking;
import org.example.trainsystem.models.Route;
import org.example.trainsystem.models.Train;
import org.example.trainsystem.services.AdminService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminController.class)
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminService adminService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void addTrain_Success() throws Exception {
        Train train = new Train();
        train.setId(1L);
        train.setTrainNumber("T100");
        train.setTotalSeats(100);

        Mockito.when(adminService.addTrain(any(Train.class))).thenReturn(train);

        mockMvc.perform(post("/api/admin/trains")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(train)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.trainNumber").value("T100"));
    }

    @Test
    void getAllTrains_Success() throws Exception {
        Train train = new Train();
        train.setId(1L);
        train.setTrainNumber("T100");

        Mockito.when(adminService.getAllTrains()).thenReturn(Arrays.asList(train));

        mockMvc.perform(get("/api/admin/trains"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void deleteTrain_Success() throws Exception {
        mockMvc.perform(delete("/api/admin/trains/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Train deleted successfully."));
        Mockito.verify(adminService, Mockito.times(1)).deleteTrain(1L);
    }

    @Test
    void deleteTrain_Exception() throws Exception {
        Mockito.doThrow(new RuntimeException("Train has bookings")).when(adminService).deleteTrain(1L);

        mockMvc.perform(delete("/api/admin/trains/1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Train has bookings"));
    }

    @Test
    void addRoute_Success() throws Exception {
        Train train = new Train();
        train.setId(1L);
        train.setTrainNumber("T100");
        train.setTotalSeats(100);
        
        Route route = new Route();
        route.setId(1L);
        route.setName("Route 1");
        route.setTrain(train);

        Mockito.when(adminService.addRoute(any(Route.class))).thenReturn(route);

        mockMvc.perform(post("/api/admin/routes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(route)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void getAllRoutes_Success() throws Exception {
        Route route = new Route();
        route.setId(1L);
        route.setName("Route 1");

        Mockito.when(adminService.getAllRoutes()).thenReturn(Arrays.asList(route));

        mockMvc.perform(get("/api/admin/routes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void deleteRoute_Success() throws Exception {
        mockMvc.perform(delete("/api/admin/routes/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Route deleted successfully."));
        Mockito.verify(adminService, Mockito.times(1)).deleteRoute(1L);
    }

    @Test
    void getBookingsForTrain_Success() throws Exception {
        Booking booking = new Booking();
        booking.setId(1L);

        Mockito.when(adminService.getBookingsForTrain(1L)).thenReturn(Arrays.asList(booking));

        mockMvc.perform(get("/api/admin/bookings/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void notifyDelay_Success() throws Exception {
        mockMvc.perform(post("/api/admin/notify-delay")
                        .param("trainId", "1")
                        .param("delayTime", "30 mins"))
                .andExpect(status().isOk())
                .andExpect(content().string("Notifications sent to all passengers of train 1"));

        Mockito.verify(adminService, Mockito.times(1)).notifyDelay(1L, "30 mins");
    }

    @Test
    void updateTrain_Success() throws Exception {
        Train train = new Train();
        train.setTrainNumber("T100");
        train.setTotalSeats(150);

        Train updatedTrain = new Train();
        updatedTrain.setId(1L);
        updatedTrain.setTrainNumber("T100");
        updatedTrain.setTotalSeats(150);

        Mockito.when(adminService.updateTrainSeats(eq(1L), eq(150))).thenReturn(updatedTrain);

        mockMvc.perform(put("/api/admin/trains/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(train)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalSeats").value(150));
    }

    @Test
    void addSimpleRoute_Success() throws Exception {
        RouteRequestDTO request = new RouteRequestDTO();

        mockMvc.perform(post("/api/admin/routes/simple")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Route and all stops created successfully!"));
        Mockito.verify(adminService, Mockito.times(1)).addRouteWithStations(any(RouteRequestDTO.class));
    }

    @Test
    void getAllBookings_Success() throws Exception {
        AdminBookingDTO dto = new AdminBookingDTO(1L, "email@example.com", "T100", "Start", "End", 2, "2023-10-10");

        Mockito.when(adminService.getAllBookingsForAdmin()).thenReturn(Arrays.asList(dto));

        mockMvc.perform(get("/api/admin/bookings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }
}
