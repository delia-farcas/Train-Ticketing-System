package org.example.trainsystem.dto;

import lombok.Data;

import java.util.List;

@Data
public class RouteRequestDTO {
    private Long trainId;
    private String routeName;
    private List<StopRequestDTO> stops;
}
