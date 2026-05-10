package org.example.trainsystem.controllers;

import jakarta.validation.Valid;
import org.example.trainsystem.dto.AdminBookingDTO;
import org.example.trainsystem.dto.RouteRequestDTO;
import org.example.trainsystem.models.Booking;
import org.example.trainsystem.models.Route;
import org.example.trainsystem.models.Train;
import org.example.trainsystem.services.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/trains")
    public ResponseEntity<Train> addTrain(@Valid @RequestBody Train train) {
        return ResponseEntity.ok(adminService.addTrain(train));
    }

    @GetMapping("/trains")
    public ResponseEntity<List<Train>> getAllTrains() {
        return ResponseEntity.ok(adminService.getAllTrains());
    }

    @PostMapping("/routes")
    public ResponseEntity<Route> addRoute(@Valid @RequestBody Route route) {
        return ResponseEntity.ok(adminService.addRoute(route));
    }

    @GetMapping("/routes")
    public ResponseEntity<List<Route>> getAllRoutes() {
        return ResponseEntity.ok(adminService.getAllRoutes());
    }

    @DeleteMapping("/trains/{id}")
    public ResponseEntity<String> deleteTrain(@PathVariable Long id) {
        try {
            adminService.deleteTrain(id);
            return ResponseEntity.ok("Train deleted successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @DeleteMapping("/routes/{id}")
    public ResponseEntity<String> deleteRoute(@PathVariable Long id) {
        try {
            adminService.deleteRoute(id);
            return ResponseEntity.ok("Route deleted successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @GetMapping("/bookings/{trainId}")
    public ResponseEntity<List<Booking>> getBookingsForTrain(@PathVariable Long trainId) {
        return ResponseEntity.ok(adminService.getBookingsForTrain(trainId));
    }

    @PostMapping("/notify-delay")
    public ResponseEntity<String> notifyDelay(@RequestParam Long trainId, @RequestParam String delayTime) {
        adminService.notifyDelay(trainId, delayTime);
        return ResponseEntity.ok("Notifications sent to all passengers of train " + trainId);
    }

    @PutMapping("/trains/{id}")
    public ResponseEntity<Train> updateTrain(@PathVariable Long id,@Valid @RequestBody Train trainDetails) {
        return ResponseEntity.ok(adminService.updateTrainSeats(id, trainDetails.getTotalSeats()));
    }

    @PostMapping("/routes/simple")
    public ResponseEntity<String> addSimpleRoute(@Valid @RequestBody RouteRequestDTO request) {
        try {
            adminService.addRouteWithStations(request);
            return ResponseEntity.ok("Route and all stops created successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Error creating route: " + e.getMessage());
        }
    }

    @GetMapping("/bookings")
    public ResponseEntity<List<AdminBookingDTO>> getAllBookings() {
        return ResponseEntity.ok(adminService.getAllBookingsForAdmin());
    }
}