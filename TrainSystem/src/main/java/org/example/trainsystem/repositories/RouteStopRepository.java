package org.example.trainsystem.repositories;

import org.example.trainsystem.models.RouteStop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RouteStopRepository extends JpaRepository<RouteStop, Long> {
    List<RouteStop> findByStationNameIgnoreCase(String stationName);

    List<RouteStop> findByRouteIdOrderByStopOrderAsc(Long routeId);
}
