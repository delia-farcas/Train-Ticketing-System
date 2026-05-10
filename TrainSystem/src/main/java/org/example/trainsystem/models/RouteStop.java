package org.example.trainsystem.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RouteStop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "route_id")
    @JsonIgnore
    private Route route;

    @ManyToOne
    @JoinColumn(name = "station_id")
    private Station station;

    private LocalTime arrivalTime;
    private LocalTime departureTime;

    @Min(value = 0, message = "Stop order must be positive")
    private int stopOrder;
}
