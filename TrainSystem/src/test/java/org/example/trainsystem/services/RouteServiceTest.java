package org.example.trainsystem.services;

import org.example.trainsystem.dto.RouteResponseDTO;
import org.example.trainsystem.models.Route;
import org.example.trainsystem.models.RouteStop;
import org.example.trainsystem.models.Station;
import org.example.trainsystem.models.Train;
import org.example.trainsystem.repositories.RouteStopRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class RouteServiceTest {

    @Mock
    private RouteStopRepository routeStopRepository;

    @InjectMocks
    private RouteService routeService;

    @Test
    void findRoutes_DirectConnection_Success() {
        Train train = new Train();
        train.setId(1L);
        train.setTrainNumber("T100");

        Route route = new Route();
        route.setId(1L);
        route.setTrain(train);

        RouteStop startStop = new RouteStop();
        startStop.setRoute(route);
        startStop.setStopOrder(1);
        startStop.setDepartureTime(LocalTime.of(10, 0));

        RouteStop endStop = new RouteStop();
        endStop.setRoute(route);
        endStop.setStopOrder(2);
        endStop.setArrivalTime(LocalTime.of(12, 0));

        Mockito.when(routeStopRepository.findByStationNameIgnoreCase("A")).thenReturn(Arrays.asList(startStop));
        Mockito.when(routeStopRepository.findByStationNameIgnoreCase("B")).thenReturn(Arrays.asList(endStop));

        List<RouteResponseDTO> result = routeService.findRoutes("A", "B");

        assertEquals(1, result.size());
        assertEquals("T100", result.get(0).getTrainNumber());
    }

    @Test
    void findRoutes_NoConnection_ThrowsException() {
        Mockito.when(routeStopRepository.findByStationNameIgnoreCase("A")).thenReturn(Collections.emptyList());
        Mockito.when(routeStopRepository.findByStationNameIgnoreCase("B")).thenReturn(Collections.emptyList());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> routeService.findRoutes("A", "B"));
        assertTrue(exception.getMessage().contains("We could not find routes"));
    }

    @Test
    void findRoutes_ConnectionWithChange_Success() {
        Train train1 = new Train();
        train1.setId(1L);
        train1.setTrainNumber("T1");

        Train train2 = new Train();
        train2.setId(2L);
        train2.setTrainNumber("T2");

        Route route1 = new Route();
        route1.setId(1L);
        route1.setTrain(train1);

        Route route2 = new Route();
        route2.setId(2L);
        route2.setTrain(train2);

        Station transferStation = new Station();
        transferStation.setName("B"); // Needs to be 'B' to match buggy RouteService implementation

        RouteStop startStop = new RouteStop();
        startStop.setRoute(route1);
        startStop.setStopOrder(1);
        startStop.setDepartureTime(LocalTime.of(10, 0));
        startStop.setStation(new Station());
        startStop.getStation().setName("A");

        RouteStop transferPoint1 = new RouteStop();
        transferPoint1.setRoute(route1);
        transferPoint1.setStopOrder(2);
        transferPoint1.setStation(transferStation);
        transferPoint1.setArrivalTime(LocalTime.of(11, 0));
        route1.setStops(Arrays.asList(startStop, transferPoint1));

        RouteStop endStop = new RouteStop();
        endStop.setRoute(route2);
        endStop.setStopOrder(2);
        endStop.setStation(new Station());
        endStop.getStation().setName("B");
        endStop.setArrivalTime(LocalTime.of(14, 0));

        Mockito.when(routeStopRepository.findByStationNameIgnoreCase("A")).thenReturn(Arrays.asList(startStop));
        Mockito.when(routeStopRepository.findByStationNameIgnoreCase("B")).thenReturn(Arrays.asList(endStop));

        List<RouteResponseDTO> result = routeService.findRoutes("A", "B");

        assertEquals(1, result.size());
        assertTrue(result.get(0).getArrivalTime().contains("via B"));
    }
}
