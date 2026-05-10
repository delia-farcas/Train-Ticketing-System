package org.example.trainsystem.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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

    @NotBlank(message = "Email is required")
    @Email(message = "Must be a valid email")
    private String passengerEmail;

    @ManyToOne
    private Train train;

    @ManyToOne
    @JoinColumn(name = "departure_station_id")
    @NotNull(message = "Departure station is required")
    private Station departureStation;

    @ManyToOne
    @JoinColumn(name = "arrival_station_id")
    @NotNull(message = "Arrival station is required")
    private Station arrivalStation;

    @NotNull(message = "Travel date is required")
    @FutureOrPresent(message = "Travel date cannot be in the past")
    private LocalDate travelDate;

    @Min(value = 1, message = "Must reserve at least 1 seat")
    @Max(value = 10, message = "Cannot reserve more than 10 seats at once")
    private int seatsReserved;

}
