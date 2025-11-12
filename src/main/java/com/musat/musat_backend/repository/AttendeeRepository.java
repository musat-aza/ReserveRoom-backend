package com.musat.musat_backend.repository;

import com.musat.musat_backend.entity.Attendee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendeeRepository extends JpaRepository<Attendee, Long> {
}