package org.example.trainsystem.services;

import org.example.trainsystem.dto.BookingRequestDTO;
import org.example.trainsystem.models.Booking;
import org.example.trainsystem.models.Station;
import org.example.trainsystem.models.Train;
import org.example.trainsystem.repositories.BookingRepository;
import org.example.trainsystem.repositories.StationRepository;
import org.example.trainsystem.repositories.TrainRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private TrainRepository trainRepository;

    @Mock
    private StationRepository stationRepository;

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private BookingService bookingService;

    @Test
    void createBooking_Success() {
        BookingRequestDTO request = new BookingRequestDTO();
        request.setTrainId(1L);
        request.setTravelDate(LocalDate.now());
        request.setNumberOfSeats(2);
        request.setDepartureStationName("Start");
        request.setArrivalStationName("End");
        request.setPassengerEmail("test@example.com");

        Train train = new Train();
        train.setId(1L);
        train.setTrainNumber("T100");
        train.setTotalSeats(100);

        Mockito.when(trainRepository.findById(1L)).thenReturn(Optional.of(train));
        Mockito.when(bookingRepository.findByTrainIdAndTravelDate(eq(1L), any(LocalDate.class)))
                .thenReturn(Collections.emptyList());

        Station start = new Station();
        start.setName("Start");
        Mockito.when(stationRepository.findByNameIgnoreCase("Start")).thenReturn(Optional.of(start));

        Station end = new Station();
        end.setName("End");
        Mockito.when(stationRepository.findByNameIgnoreCase("End")).thenReturn(Optional.of(end));

        Booking booking = new Booking();
        booking.setTrain(train);
        booking.setDepartureStation(start);
        booking.setArrivalStation(end);

        Mockito.when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        Booking result = bookingService.createBooking(request);

        assertNotNull(result);
        Mockito.verify(bookingRepository).save(any(Booking.class));
        Mockito.verify(mailSender).send(any(SimpleMailMessage.class));
    }

    @Test
    void createBooking_NotEnoughSeats_ThrowsException() {
        BookingRequestDTO request = new BookingRequestDTO();
        request.setTrainId(1L);
        request.setTravelDate(LocalDate.now());
        request.setNumberOfSeats(50);

        Train train = new Train();
        train.setId(1L);
        train.setTotalSeats(100);

        Booking existingBooking = new Booking();
        existingBooking.setSeatsReserved(60);

        Mockito.when(trainRepository.findById(1L)).thenReturn(Optional.of(train));
        Mockito.when(bookingRepository.findByTrainIdAndTravelDate(eq(1L), any(LocalDate.class)))
                .thenReturn(Collections.singletonList(existingBooking));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> bookingService.createBooking(request));
        assertTrue(exception.getMessage().contains("Not enough places"));
    }

    @Test
    void createBooking_TrainNotFound_ThrowsException() {
        BookingRequestDTO request = new BookingRequestDTO();
        request.setTrainId(1L);

        Mockito.when(trainRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> bookingService.createBooking(request));
        assertEquals("Train not found", exception.getMessage());
    }
}
