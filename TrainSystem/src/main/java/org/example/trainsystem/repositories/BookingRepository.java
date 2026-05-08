package org.example.trainsystem.repositories;

import org.example.trainsystem.models.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking,Long> {
    List<Booking> findByTrainIdAndTravelDate(Long trainId, LocalDate travelDate);


    List<Booking> findByTrainId(Long trainId);
}
