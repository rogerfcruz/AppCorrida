package com.AppCorrida.AppCorrida.repositories;

import com.AppCorrida.AppCorrida.entities.Ride;
import com.AppCorrida.AppCorrida.entities.enums.RideStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RideRepository extends JpaRepository<Ride, Long> {
    boolean existsByPassengerIdAndStatus(Long passengerId, RideStatus status);
    List<Ride> findByDriverIdAndStatus(Long driverId, RideStatus status);
    List<Ride> findByStatus(RideStatus status);
}