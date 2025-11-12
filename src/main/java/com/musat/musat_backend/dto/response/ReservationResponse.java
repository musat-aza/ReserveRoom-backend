package com.musat.musat_backend.dto.response;

import com.musat.musat_backend.entity.Reservation;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ReservationResponse {

    private Integer reservationId;
    private Integer userId;

    private String roomName;
    private String bookerName;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String purpose;

    private List<String> attendeeNames;

    public ReservationResponse(Reservation reservation) {
        this.reservationId = reservation.getId();

        this.userId = reservation.getUser().getId();

        this.roomName = reservation.getRoom().getName();
        this.bookerName = reservation.getUser().getName();

        this.startTime = reservation.getStartTime();
        this.endTime = reservation.getEndTime();
        this.purpose = reservation.getPurpose().name();

        this.attendeeNames = reservation.getAttendees().stream()
                .map(attendee -> attendee.getUser().getName()) // 여기도 getUsername() 확인
                .collect(Collectors.toList());
    }
}