package org.example.trainsystem.services;


import org.example.trainsystem.models.RouteStop;
import org.example.trainsystem.repositories.RouteStopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RouteService {

    private final RouteStopRepository routeStopRepository;

    public List<String> findRoutes(String fromStation, String toStation) {
        List<String> results = new ArrayList<>();

        List<RouteStop> departureStops = routeStopRepository.findByStationName(fromStation);
        List<RouteStop> arrivalStops = routeStopRepository.findByStationName(toStation);

        for (RouteStop dep : departureStops) {
            for (RouteStop arr : arrivalStops) {
                if (dep.getRoute().getId().equals(arr.getRoute().getId()) && dep.getStopOrder() < arr.getStopOrder()) {
                    results.add("Direct: Train " + dep.getRoute().getTrain().getTrainNumber() +
                            " | Departure: " + dep.getDepartureTime() + " -> Arrival: " + arr.getArrivalTime());
                }
            }
        }

        if (results.isEmpty()) {
            results.addAll(findConnectionWithChange(fromStation, toStation));
        }

        if (results.isEmpty()) {
            throw new RuntimeException("We are sorry! We could not find direct/changing routes from " + fromStation + " to " + toStation);
        }

        return results;
    }

    private List<String> findConnectionWithChange(String from, String to) {
        List<String> connections = new ArrayList<>();
        List<RouteStop> departuresFromStart = routeStopRepository.findByStationName(from);
        List<RouteStop> arrivalsAtEnd = routeStopRepository.findByStationName(to);

        for (RouteStop startStop : departuresFromStart) {
            List<RouteStop> intermediateStops = startStop.getRoute().getStops().stream()
                    .filter(s -> s.getStopOrder() > startStop.getStopOrder())
                    .collect(Collectors.toList());

            for (RouteStop inter : intermediateStops) {
                for (RouteStop endStop : arrivalsAtEnd) {
                    if (endStop.getStation().getName().equals(inter.getStation().getName()) &&
                            endStop.getRoute().getId() != startStop.getRoute().getId()) {

                        connections.add("Change in " + inter.getStation().getName() + ": " +
                                "Tren 1 (" + startStop.getRoute().getTrain().getTrainNumber() + ") -> " +
                                "Tren 2 (" + endStop.getRoute().getTrain().getTrainNumber() + ")");
                    }
                }
            }
        }
        return connections;
    }
}
