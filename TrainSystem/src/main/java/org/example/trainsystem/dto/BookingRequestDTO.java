package org.example.trainsystem.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class BookingRequestDTO {
    private String passengerEmail;
    private Long trainId;
    private String departureStationName;
    private String arrivalStationName;
    private LocalDate travelDate;
    private int numberOfSeats;
}
