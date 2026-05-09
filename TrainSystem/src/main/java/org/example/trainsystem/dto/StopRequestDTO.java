package org.example.trainsystem.dto;

import lombok.Data;

@Data
public class StopRequestDTO {
    private String stationName;
    private int stopOrder;
    private String arrivalTime;
    private String departureTime;
}