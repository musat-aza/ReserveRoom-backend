package com.musat.musat_backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "attendees")
public class Attendee {

    // [수정됨] attendees_id 대신 복합 키(Composite Key) 사용
    @EmbeddedId
    private AttendeeId id;

    // [수정됨] ID 클래스의 필드와 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("reservationId") // AttendeeId의 reservationId 필드에 매핑
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    // [수정됨] ID 클래스의 필드와 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId") // AttendeeId의 userId 필드에 매핑
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Attendee(Reservation reservation, User user) {
        // [수정됨] 복합 키 자동 생성
        this.id = new AttendeeId(reservation.getId(), user.getId());
        this.reservation = reservation;
        this.user = user;
    }
}