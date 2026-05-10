package org.example.trainsystem.repositories;

import org.example.trainsystem.models.RouteStop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RouteStopRepository extends JpaRepository<RouteStop, Long> {

    @Query("""
    SELECT rs FROM RouteStop rs
    JOIN FETCH rs.route r
    JOIN FETCH r.train
    JOIN FETCH r.stops s
    JOIN FETCH s.station
    WHERE LOWER(rs.station.name) = LOWER(:name)
    """)
    List<RouteStop> findByStationNameIgnoreCase(@Param("name") String stationName);

    List<RouteStop> findByRouteIdOrderByStopOrderAsc(Long routeId);
}
