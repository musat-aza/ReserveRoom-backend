package com.musat.musat_backend.service;

import com.musat.musat_backend.dto.request.ReservationDto;
import com.musat.musat_backend.dto.response.ReservationResponse;
import com.musat.musat_backend.entity.*;
import com.musat.musat_backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.stream.Collectors;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
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

    public ReservationResponse getReservation(Integer reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("예약을 찾을 수 없습니다. id=" + reservationId));
        return new ReservationResponse(reservation);
    }

    public List<ReservationResponse> getAllReservations() {
        return reservationRepository.findAll().stream()
                .map(ReservationResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public ReservationResponse updateReservation(Integer reservationId, ReservationDto requestDto) {
        // 1. 수정할 예약 조회
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("예약을 찾을 수 없습니다. id=" + reservationId));

        // 2. 연관 엔티티 (User, Room) 다시 조회
        User booker = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다. id=" + requestDto.getUserId()));

        Room room = roomRepository.findById(requestDto.getRoomId())
                .orElseThrow(() -> new IllegalArgumentException("회의실을 찾을 수 없습니다. id=" + requestDto.getRoomId()));

        // 3. 기존 동반자 목록(Attendees) 삭제
        reservation.getAttendees().clear(); // orphanRemoval=true에 의해 DB에서 삭제됨

        // 4. 새로운 동반자 목록 추가
        if (requestDto.getAttendeeIds() != null) {
            List<User> attendeeUsers = userRepository.findAllById(requestDto.getAttendeeIds());
            for (User attendeeUser : attendeeUsers) {
                Attendee newAttendee = Attendee.builder()
                        .reservation(reservation)
                        .user(attendeeUser)
                        .build();
                reservation.getAttendees().add(newAttendee); // Cascade로 자동 저장
            }
        }

        // 5. 예약 정보 업데이트 [수정됨]
        reservation.update(
                booker,
                room,
                requestDto.getStartTime(),
                requestDto.getEndTime(),
                requestDto.getPurpose()
        );

        return new ReservationResponse(reservation);
    }

    // [추가] 3. 예약 삭제
    @Transactional
    public void deleteReservation(Integer reservationId) {
        if (!reservationRepository.existsById(reservationId)) {
            throw new IllegalArgumentException("예약을 찾을 수 없습니다. id=" + reservationId);
        }
        reservationRepository.deleteById(reservationId);
    }
}