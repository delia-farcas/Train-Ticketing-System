package org.example.trainsystem.services;

import org.example.trainsystem.models.Booking;
import org.example.trainsystem.models.Train;
import org.example.trainsystem.repositories.BookingRepository;
import org.example.trainsystem.repositories.TrainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final BookingRepository bookingRepository;
    private final TrainRepository trainRepository;
    private final JavaMailSender mailSender;


    public Train addTrain(Train train) {
        return trainRepository.save(train);
    }

    public void deleteTrain(Long trainId) {
        trainRepository.deleteById(trainId);
    }

    public List<Train> getAllTrains() {
        return trainRepository.findAll();
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
}