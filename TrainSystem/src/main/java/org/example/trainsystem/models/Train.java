package org.example.trainsystem.models;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Train {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Train number is required")
    @Pattern(regexp = "^[A-Z0-9\\-]{2,10}$", message = "Train number must be 2-10 uppercase letters/digits")
    @Column(nullable = false, unique = true)
    private String trainNumber;

    @Min(value = 1, message = "Must have at least 1 seat")
    @Max(value = 2000, message = "Seat count seems unrealistic")
    private int totalSeats;
}
