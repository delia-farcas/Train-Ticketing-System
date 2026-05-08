package org.example.trainsystem.models;

import jakarta.persistence.*;
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
    private Route route;

    @ManyToOne
    @JoinColumn(name = "station_id")
    private Station station;

    private LocalTime arrivalTime;
    private LocalTime departureTime;

    private int stopOrder;
}
