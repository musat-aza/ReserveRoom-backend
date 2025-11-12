package com.musat.musat_backend.service;

import com.musat.musat_backend.dto.request.ReservationDto;
import com.musat.musat_backend.dto.response.ReservationResponse;
import com.musat.musat_backend.entity.*;
import com.musat.musat_backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final AttendeeRepository attendeeRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;

    @Transactional
    public ReservationResponse createReservation(ReservationDto request) {

        User booker = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다. id=" + request.getUserId()));

        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new IllegalArgumentException("회의실을 찾을 수 없습니다. id=" + request.getRoomId()));

        Reservation reservation = request.toEntity(booker, room);

        reservationRepository.save(reservation);

        if (request.getAttendeeIds() != null) {
            List<User> attendees = userRepository.findAllById(request.getAttendeeIds());
            for (User attendeeUser : attendees) {
                Attendee attendee = Attendee.builder()
                        .reservation(reservation)
                        .user(attendeeUser)
                        .build();
                attendeeRepository.save(attendee);
            }
        }

        return new ReservationResponse(reservation);
    }

    public ReservationResponse getReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("예약을 찾을 수 없습니다."));
        return new ReservationResponse(reservation);
    }
}