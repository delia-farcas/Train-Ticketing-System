package org.example.trainsystem.services;

import lombok.RequiredArgsConstructor;
import org.example.trainsystem.dto.BookingRequest;
import org.example.trainsystem.models.Booking;
import org.example.trainsystem.models.Station;
import org.example.trainsystem.models.Train;
import org.example.trainsystem.repositories.BookingRepository;
import org.example.trainsystem.repositories.StationRepository;
import org.example.trainsystem.repositories.TrainRepository;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final TrainRepository trainRepository;
    private final StationRepository stationRepository;
    private final JavaMailSender mailSender;

    @Transactional
    public Booking createBooking(BookingRequest request) {
        Train train = trainRepository.findById(request.getTrainId())
                .orElseThrow(() -> new RuntimeException("Train not found"));

        int occupiedSeats = bookingRepository.findByTrainIdAndTravelDate(train.getId(), request.getTravelDate())
                .stream()
                .mapToInt(Booking::getSeatsReserved)
                .sum();

        if (occupiedSeats + request.getNumberOfSeats() > train.getTotalSeats()) {
            throw new RuntimeException("Not enough places availavle. Actual number of available places:" + (train.getTotalSeats() - occupiedSeats));
        }

        Station depStation = stationRepository.findById(request.getDepartureStationId()).get();
        Station arrStation = stationRepository.findById(request.getArrivalStationId()).get();

        Booking booking = Booking.builder()
                .passengerEmail(request.getPassengerEmail())
                .train(train)
                .departureStation(depStation)
                .arrivalStation(arrStation)
                .travelDate(request.getTravelDate())
                .seatsReserved(request.getNumberOfSeats())
                .build();

        Booking savedBooking = bookingRepository.save(booking);

        sendConfirmationEmail(savedBooking);

        return savedBooking;
    }

    private void sendConfirmationEmail(Booking booking) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(booking.getPassengerEmail());
        message.setSubject("Train Booking Confirmation");
        message.setText("Your booking for the train " + booking.getTrain().getTrainNumber() +
                " from" + booking.getDepartureStation().getName() +
                " to " + booking.getArrivalStation().getName() + " is confirmed!");

        mailSender.send(message);
    }
}
