package org.example.trainsystem.services;


import org.example.trainsystem.dto.RouteResponseDTO;
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

    public List<RouteResponseDTO> findRoutes(String fromStation, String toStation) {
        List<RouteResponseDTO> results = new ArrayList<>();

        List<RouteStop> departureStops = routeStopRepository.findByStationNameIgnoreCase(fromStation);
        List<RouteStop> arrivalStops = routeStopRepository.findByStationNameIgnoreCase(toStation);

        for (RouteStop dep : departureStops) {
            for (RouteStop arr : arrivalStops) {
                if (dep.getRoute().getId().equals(arr.getRoute().getId()) && dep.getStopOrder() < arr.getStopOrder()) {
                    results.add(new RouteResponseDTO(
                            dep.getRoute().getTrain().getId(),
                            dep.getRoute().getTrain().getTrainNumber(),
                            fromStation,
                            toStation,
                            dep.getDepartureTime().toString(),
                            arr.getArrivalTime().toString()
                    ));
                }
            }
        }

        if (results.isEmpty()) {
            results.addAll(findConnectionWithChange(fromStation, toStation));
        }

        if (results.isEmpty()) {
            throw new RuntimeException("We are sorry! We could not find routes from " + fromStation + " to " + toStation);
        }

        return results;
    }

    private List<RouteResponseDTO> findConnectionWithChange(String from, String to) {
        List<RouteResponseDTO> connections = new ArrayList<>();
        List<RouteStop> departuresFromStart = routeStopRepository.findByStationNameIgnoreCase(from);
        List<RouteStop> arrivalsAtEnd = routeStopRepository.findByStationNameIgnoreCase(to);

        for (RouteStop startStop : departuresFromStart) {
            List<RouteStop> possibleTransferStops = startStop.getRoute().getStops().stream()
                    .filter(s -> s.getStopOrder() > startStop.getStopOrder())
                    .collect(Collectors.toList());

            for (RouteStop transferPoint : possibleTransferStops) {
                for (RouteStop endStop : arrivalsAtEnd) {
                    if (endStop.getStation().getName().equalsIgnoreCase(transferPoint.getStation().getName()) &&
                            !endStop.getRoute().getId().equals(startStop.getRoute().getId())) {

                        String detail = "Change in " + transferPoint.getStation().getName() +
                                " (Tren 1: " + startStop.getRoute().getTrain().getTrainNumber() +
                                " ➔ Tren 2: " + endStop.getRoute().getTrain().getTrainNumber() + ")";

                        connections.add(new RouteResponseDTO(
                                startStop.getRoute().getTrain().getId(),
                                startStop.getRoute().getTrain().getTrainNumber(),
                                from,
                                to,
                                startStop.getDepartureTime().toString(),
                                endStop.getArrivalTime().toString() + " (via " + transferPoint.getStation().getName() + ")"
                        ));
                    }
                }
            }
        }
        return connections;
    }
}
