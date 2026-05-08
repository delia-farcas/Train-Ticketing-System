package org.example.trainsystem.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class BookingRequest {
    private String passengerEmail;
    private Long trainId;
    private Long departureStationId;
    private Long arrivalStationId;
    private LocalDate travelDate;
    private int numberOfSeats;
}
