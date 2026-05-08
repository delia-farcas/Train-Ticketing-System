package org.example.trainsystem.models;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    private Train train;

    @OneToMany(mappedBy = "route", cascade = CascadeType.ALL)
    @OrderBy("stopOrder ASC")
    private List<RouteStop> stops;
}
