package org.example.trainsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdminBookingDTO {
    private Long id;
    private String passengerEmail;
    private String trainNumber;
    private String departureStationName;
    private String arrivalStationName;
    private int numberOfSeats;
    private String travelDate;
}
