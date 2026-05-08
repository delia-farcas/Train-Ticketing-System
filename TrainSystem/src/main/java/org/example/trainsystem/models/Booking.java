package org.example.trainsystem.models;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"train", "departureStation", "arrivalStation"})
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String passengerEmail;

    @ManyToOne
    private Train train;

    @ManyToOne
    @JoinColumn(name = "departure_station_id")
    private Station departureStation;

    @ManyToOne
    @JoinColumn(name = "arrival_station_id")
    private Station arrivalStation;

    private LocalDate travelDate;
    private int seatsReserved;
}
