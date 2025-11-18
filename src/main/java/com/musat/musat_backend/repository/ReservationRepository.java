package com.musat.musat_backend.repository;

import com.musat.musat_backend.entity.Reservation;
import com.musat.musat_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    List<Reservation> findAllByUserOrderByStartTimeDesc(User user);
}