package com.musat.musat_backend.dto.request;

import com.musat.musat_backend.entity.Reservation;
import com.musat.musat_backend.entity.ReservationPurpose;
import com.musat.musat_backend.entity.Room;
import com.musat.musat_backend.entity.User;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor()
@AllArgsConstructor
@Builder
public class ReservationDto {

    private Integer roomId;
    private Integer userId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private ReservationPurpose purpose;

    private List<Integer> attendeeIds;

    public Reservation toEntity(User user, Room room) {
        return Reservation.builder()
                .user(user)
                .room(room)
                .startTime(this.startTime)
                .endTime(this.endTime)
                .purpose(this.purpose)
                .build();
    }
}