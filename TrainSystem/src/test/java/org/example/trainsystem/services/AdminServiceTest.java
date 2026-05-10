package org.example.trainsystem.services;

import org.example.trainsystem.dto.AdminBookingDTO;
import org.example.trainsystem.dto.RouteRequestDTO;
import org.example.trainsystem.dto.StopRequestDTO;
import org.example.trainsystem.models.*;
import org.example.trainsystem.repositories.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
public class AdminServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private TrainRepository trainRepository;

    @Mock
    private RouteRepository routeRepository;

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private StationRepository stationRepository;

    @Mock
    private RouteStopRepository routeStopRepository;

    @InjectMocks
    private AdminService adminService;

    @Test
    void addTrain_Success() {
        Train train = new Train();
        Mockito.when(trainRepository.save(train)).thenReturn(train);

        Train result = adminService.addTrain(train);
        assertNotNull(result);
        Mockito.verify(trainRepository).save(train);
    }

    @Test
    void deleteTrain_Success() {
        Train train = new Train();
        train.setTrainNumber("T1");
        Mockito.when(trainRepository.findById(1L)).thenReturn(Optional.of(train));
        Mockito.when(bookingRepository.findByTrainId(1L)).thenReturn(Collections.emptyList());

        adminService.deleteTrain(1L);

        Mockito.verify(trainRepository).delete(train);
    }

    @Test
    void deleteTrain_HasBookings_ThrowsException() {
        Train train = new Train();
        train.setTrainNumber("T1");
        Mockito.when(trainRepository.findById(1L)).thenReturn(Optional.of(train));
        Mockito.when(bookingRepository.findByTrainId(1L)).thenReturn(Arrays.asList(new Booking()));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> adminService.deleteTrain(1L));
        assertTrue(exception.getMessage().contains("Cannot delete train"));
    }

    @Test
    void getAllTrains_Success() {
        Mockito.when(trainRepository.findAll()).thenReturn(Arrays.asList(new Train()));

        List<Train> trains = adminService.getAllTrains();
        assertEquals(1, trains.size());
    }

    @Test
    void addRoute_Success() {
        Route route = new Route();
        Mockito.when(routeRepository.save(route)).thenReturn(route);

        Route result = adminService.addRoute(route);
        assertNotNull(result);
    }

    @Test
    void deleteRoute_Success() {
        Route route = new Route();
        Mockito.when(routeRepository.findById(1L)).thenReturn(Optional.of(route));

        adminService.deleteRoute(1L);

        Mockito.verify(routeRepository).delete(route);
    }

    @Test
    void getAllRoutes_Success() {
        Mockito.when(routeRepository.findAll()).thenReturn(Arrays.asList(new Route()));

        List<Route> routes = adminService.getAllRoutes();
        assertEquals(1, routes.size());
    }

    @Test
    void updateTrainSeats_Success() {
        Train train = new Train();
        train.setTotalSeats(100);
        Mockito.when(trainRepository.findById(1L)).thenReturn(Optional.of(train));
        Mockito.when(trainRepository.save(any(Train.class))).thenReturn(train);

        Train result = adminService.updateTrainSeats(1L, 150);

        assertEquals(150, result.getTotalSeats());
        Mockito.verify(trainRepository).save(train);
    }

    @Test
    void getBookingsForTrain_Success() {
        Mockito.when(bookingRepository.findByTrainId(1L)).thenReturn(Arrays.asList(new Booking()));

        List<Booking> bookings = adminService.getBookingsForTrain(1L);
        assertEquals(1, bookings.size());
    }

    @Test
    void notifyDelay_Success() {
        Booking booking = new Booking();
        booking.setPassengerEmail("test@test.com");
        Train train = new Train();
        train.setTrainNumber("T1");
        booking.setTrain(train);
        Station station = new Station();
        station.setName("Station1");
        booking.setDepartureStation(station);
        booking.setTravelDate(LocalDate.now());

        Mockito.when(bookingRepository.findByTrainId(1L)).thenReturn(Arrays.asList(booking));

        adminService.notifyDelay(1L, "30 minutes");

        Mockito.verify(mailSender).send(any(SimpleMailMessage.class));
    }

    @Test
    void getAllBookingsForAdmin_Success() {
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setPassengerEmail("email@example.com");
        Train train = new Train();
        train.setTrainNumber("T100");
        booking.setTrain(train);
        Station dep = new Station();
        dep.setName("Dep");
        Station arr = new Station();
        arr.setName("Arr");
        booking.setDepartureStation(dep);
        booking.setArrivalStation(arr);
        booking.setSeatsReserved(2);
        booking.setTravelDate(LocalDate.now());

        Mockito.when(bookingRepository.findAll()).thenReturn(Arrays.asList(booking));

        List<AdminBookingDTO> list = adminService.getAllBookingsForAdmin();
        assertEquals(1, list.size());
        assertEquals("T100", list.get(0).getTrainNumber());
    }

    @Test
    void addRouteWithStations_Success() {
        RouteRequestDTO dto = new RouteRequestDTO();
        dto.setTrainId(1L);
        dto.setRouteName("Route1");

        StopRequestDTO stop1 = new StopRequestDTO();
        stop1.setStationName("Station1");
        stop1.setStopOrder(1);
        stop1.setArrivalTime("10:00");
        stop1.setDepartureTime("10:10");
        dto.setStops(Arrays.asList(stop1));

        Train train = new Train();
        Mockito.when(trainRepository.findById(1L)).thenReturn(Optional.of(train));
        Mockito.when(routeRepository.save(any(Route.class))).thenReturn(new Route());
        
        Station station = new Station();
        station.setName("Station1");
        Mockito.when(stationRepository.findByNameIgnoreCase("Station1")).thenReturn(Optional.of(station));

        adminService.addRouteWithStations(dto);

        Mockito.verify(routeRepository).save(any(Route.class));
        Mockito.verify(routeStopRepository).save(any(RouteStop.class));
    }
}
