package org.example.trainsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RouteResponseDTO {
    private Long trainId;
    private String trainNumber;
    private String departureStation;
    private String arrivalStation;
    private String departureTime;
    private String arrivalTime;
}