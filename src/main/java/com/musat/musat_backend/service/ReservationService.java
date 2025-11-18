package com.musat.musat_backend.service;

import com.musat.musat_backend.dto.request.ReservationDto;
import com.musat.musat_backend.dto.response.ReservationResponse;
import com.musat.musat_backend.dto.response.MyReservationResponse;
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

        // 1. 예약자(User) 조회 (변수 booker 선언)
        User booker = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다. id=" + request.getUserId()));

        // 2. 회의실(Room) 조회 (변수 room 선언)
        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new IllegalArgumentException("회의실을 찾을 수 없습니다. id=" + request.getRoomId()));

        // 3. 예약(Reservation) 생성 및 저장
        Reservation reservation = request.toEntity(booker, room);
        reservationRepository.save(reservation);

        // 4. 동반자(Attendee) 처리 - 학번 리스트 사용
        if (request.getAttendeeStudentIds() != null && !request.getAttendeeStudentIds().isEmpty()) {

            // 학번들로 유저 목록 한 번에 조회
            List<User> attendees = userRepository.findAllByStudentIdIn(request.getAttendeeStudentIds());

            // (선택) 요청한 학번 수와 찾은 유저 수가 다르면 예외 처리 (없는 학번 입력 시)
            if (attendees.size() != request.getAttendeeStudentIds().size()) {
                throw new IllegalArgumentException("존재하지 않는 학번이 포함되어 있습니다.");
            }

            // 동반자 저장
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
    public ReservationResponse updateReservation(Integer reservationId, ReservationDto request) {
        // 1. 수정할 예약 조회 (이때 기존 동반자 목록(attendees)도 같이 로드됨)
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("예약을 찾을 수 없습니다. id=" + reservationId));

        // 2. 연관 엔티티 (User, Room) 다시 조회
        User booker = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다. id=" + request.getUserId()));

        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new IllegalArgumentException("회의실을 찾을 수 없습니다. id=" + request.getRoomId()));

        // --- 3. 동반자 목록 '스마트' 업데이트 (★ 여기가 핵심 ★) ---

        // (A) 요청으로 들어온 새 동반자 ID 목록 (예: [2, 4])
        List<Integer> newAttendeeIds = request.getAttendeeStudentIds();

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

        reservation.update(
                booker,
                room,
                request.getStartTime(),
                request.getEndTime(),
                request.getPurpose()
        );

        return new ReservationResponse(reservation);
    }

    @Transactional
    public void deleteReservation(Integer reservationId) {
        if (!reservationRepository.existsById(reservationId)) {
            throw new IllegalArgumentException("예약을 찾을 수 없습니다. id=" + reservationId);
        }
        reservationRepository.deleteById(reservationId);
    }

    @Transactional(readOnly = true)
    public MyReservationResponse getMyReservations(Integer userId) {
        // 1. User가 존재하는지 확인
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다. id=" + userId));

        // 2. 해당 User의 모든 예약 조회 (DB 쿼리 1번)
        List<Reservation> userReservations = reservationRepository.findAllByUserOrderByStartTimeDesc(user);

        // 3. 요약 건수 계산 (Java Stream 활용)
        long totalCount = userReservations.size();

        long cubeCount = userReservations.stream()
                .filter(r -> r.getRoom().getType() == RoomType.CUBE)
                .count();

        long smashCount = userReservations.stream()
                .filter(r -> r.getRoom().getType() == RoomType.SMASH)
                .count();

        // 4. DTO 리스트로 변환
        List<ReservationResponse> reservationListDto = userReservations.stream()
                .map(ReservationResponse::new)
                .collect(Collectors.toList());

        // 5. 최종 응답 DTO 생성 및 반환
        return new MyReservationResponse(totalCount, cubeCount, smashCount, reservationListDto);
    }
}