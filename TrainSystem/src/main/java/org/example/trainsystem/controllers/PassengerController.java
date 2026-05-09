package org.example.trainsystem.controllers;

import org.example.trainsystem.dto.BookingRequestDTO;
import org.example.trainsystem.dto.RouteResponseDTO;
import org.example.trainsystem.models.Booking;
import org.example.trainsystem.services.BookingService;
import org.example.trainsystem.services.RouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/passenger")
@RequiredArgsConstructor
public class PassengerController {

    private final RouteService routeService;
    private final BookingService bookingService;

    @GetMapping("/search")
    public ResponseEntity<List<RouteResponseDTO>> searchRoutes(@RequestParam String from, @RequestParam String to) {
        return ResponseEntity.ok(routeService.findRoutes(from, to));
    }

    @PostMapping("/book")
    public ResponseEntity<Booking> bookTicket(@RequestBody BookingRequestDTO request) {
        return ResponseEntity.ok(bookingService.createBooking(request));
    }
}