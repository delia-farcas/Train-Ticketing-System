package org.example.trainsystem.services;

import org.example.trainsystem.dto.AdminBookingDTO;
import org.example.trainsystem.dto.RouteRequestDTO;
import org.example.trainsystem.dto.StopRequestDTO;
import org.example.trainsystem.models.*;
import org.example.trainsystem.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final BookingRepository bookingRepository;
    private final TrainRepository trainRepository;
    private final RouteRepository routeRepository;
    private final JavaMailSender mailSender;
    private final StationRepository stationRepository;
    private final RouteStopRepository routeStopRepository;


    public Train addTrain(Train train) {
        return trainRepository.save(train);
    }

    public void deleteTrain(Long trainId) {
        Train train = trainRepository.findById(trainId)
                .orElseThrow(() -> new RuntimeException("Train not found with ID: " + trainId));

        List<Booking> bookings = bookingRepository.findByTrainId(trainId);
        if (!bookings.isEmpty()) {
            throw new RuntimeException("Cannot delete train " + train.getTrainNumber() +
                    " because it has " + bookings.size() + " active bookings.");
        }

        trainRepository.delete(train);
    }

    public List<Train> getAllTrains() {
        return trainRepository.findAll();
    }

    public Route addRoute(Route route) {
        return routeRepository.save(route);
    }

    public void deleteRoute(Long routeId) {
        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new RuntimeException("Route not found with ID: " + routeId));

        try {
            routeRepository.delete(route);
        } catch (Exception e) {
            throw new RuntimeException("Cannot delete route: It is linked to active bookings.");
        }
    }

    public List<Route> getAllRoutes() {
        return routeRepository.findAll();
    }

    public Train updateTrainSeats(Long trainId, int newSeatCount) {
        Train train = trainRepository.findById(trainId)
                .orElseThrow(() -> new RuntimeException("Train inexistent"));
        train.setTotalSeats(newSeatCount);
        return trainRepository.save(train);
    }


    public List<Booking> getBookingsForTrain(Long trainId) {
        return bookingRepository.findByTrainId(trainId);
    }

    @Transactional
    public void notifyDelay(Long trainId, String delayTime) {
        List<Booking> bookings = bookingRepository.findByTrainId(trainId);

        if (bookings.isEmpty()) return;

        for (Booking b : bookings) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(b.getPassengerEmail());
            message.setSubject("Train Delay Alert: " + b.getTrain().getTrainNumber());
            message.setText("Dear passenger,\n\n" +
                    "We regret to inform you that your train " + b.getTrain().getTrainNumber() +
                    " is experiencing a delay of " + delayTime + ".\n" +
                    "Departure Station: " + b.getDepartureStation().getName() + "\n" +
                    "Date: " + b.getTravelDate() + "\n\n" +
                    "We apologize for any inconvenience caused.\n" +
                    "Safe travels!");
            mailSender.send(message);
        }
    }

    public List<AdminBookingDTO> getAllBookingsForAdmin() {
        return bookingRepository.findAll().stream()
                .map(b -> new AdminBookingDTO(
                        b.getId(),
                        b.getPassengerEmail(),
                        b.getTrain().getTrainNumber(),
                        b.getDepartureStation().getName(),
                        b.getArrivalStation().getName(),
                        b.getSeatsReserved(),
                        b.getTravelDate().toString()
                )).toList();
    }

    @Transactional
    public void addRouteWithStations(RouteRequestDTO dto) {
        Train train = trainRepository.findById(dto.getTrainId())
                .orElseThrow(() -> new RuntimeException("Train not found"));

        Route route = new Route();
        route.setTrain(train);
        route.setName(dto.getRouteName());
        Route savedRoute = routeRepository.save(route);

        for (StopRequestDTO stopDto : dto.getStops()) {
            Station station = stationRepository.findByNameIgnoreCase(stopDto.getStationName())
                    .orElseThrow(() -> new RuntimeException("Station not found: " + stopDto.getStationName()));

            RouteStop rs = new RouteStop();
            rs.setRoute(savedRoute);
            rs.setStation(station);
            rs.setStopOrder(stopDto.getStopOrder());

            if (stopDto.getArrivalTime() != null && !stopDto.getArrivalTime().isEmpty()) {
                rs.setArrivalTime(LocalTime.parse(stopDto.getArrivalTime()));
            }
            if (stopDto.getDepartureTime() != null && !stopDto.getDepartureTime().isEmpty()) {
                rs.setDepartureTime(LocalTime.parse(stopDto.getDepartureTime()));
            }

            routeStopRepository.save(rs);
        }
    }
}