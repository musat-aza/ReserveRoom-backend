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
        // 1. 수정할 예약 조회 (이때 기존 동반자 목록(attendees)도 같이 로드됨)
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("예약을 찾을 수 없습니다. id=" + reservationId));

        // 2. 연관 엔티티 (User, Room) 다시 조회
        User booker = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다. id=" + requestDto.getUserId()));

        Room room = roomRepository.findById(requestDto.getRoomId())
                .orElseThrow(() -> new IllegalArgumentException("회의실을 찾을 수 없습니다. id=" + requestDto.getRoomId()));

        // --- 3. 동반자 목록 '스마트' 업데이트 (★ 여기가 핵심 ★) ---

        // (A) 요청으로 들어온 새 동반자 ID 목록 (예: [2, 4])
        List<Integer> newAttendeeIds = requestDto.getAttendeeIds();

        // (B) 기존 동반자 목록 (예: [1, 2])
        List<Attendee> currentAttendees = reservation.getAttendees();

        // (C) 삭제할 동반자 찾기: (B)에는 있지만 (A)에는 없는 것 (예: [1])
        // currentAttendees 리스트에서 직접 삭제 -> orphanRemoval=true가 DB 삭제를 트리거
        currentAttendees.removeIf(attendee ->
                !newAttendeeIds.contains(attendee.getUser().getId())
        );

        // (D) 추가할 동반자 찾기: (A)에는 있지만 (B)에는 없는 것 (예: [4])
        // 현재 DB에 저장된 동반자 ID 목록 (예: [2])
        List<Integer> currentAttendeeIds = currentAttendees.stream()
                .map(attendee -> attendee.getUser().getId())
                .collect(Collectors.toList());

        for (Integer newId : newAttendeeIds) {
            if (!currentAttendeeIds.contains(newId)) {
                // 추가해야 할 ID
                User attendeeUser = userRepository.findById(newId)
                        .orElseThrow(() -> new IllegalArgumentException("동반자 사용자를 찾을 수 없습니다. id=" + newId));

                Attendee newAttendee = Attendee.builder()
                        .reservation(reservation)
                        .user(attendeeUser)
                        .build();

                currentAttendees.add(newAttendee); // -> cascade=ALL이 DB 저장을 트리거
            }
        }

        // ----------------------------------------------------

        // 4. 예약 정보 업데이트
        reservation.update(
                booker,
                room,
                requestDto.getStartTime(),
                requestDto.getEndTime(),
                requestDto.getPurpose()
        );

        // 5. Transaction이 종료될 때 JPA가 (C)와 (D)의 변경사항을 DB에 자동 반영
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