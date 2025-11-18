package com.musat.musat_backend.repository;

import com.musat.musat_backend.entity.Attendee;
import com.musat.musat_backend.entity.AttendeeId; // [추가]
import org.springframework.data.jpa.repository.JpaRepository;
import com.musat.musat_backend.entity.Reservation;

// [수정됨] ID 타입을 복합 키 클래스(AttendeeId)로 변경
public interface AttendeeRepository extends JpaRepository<Attendee, AttendeeId> {
    void deleteAllByReservation(Reservation reservation);
}