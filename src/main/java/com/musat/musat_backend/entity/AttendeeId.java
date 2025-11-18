package com.musat.musat_backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class AttendeeId implements Serializable {

    @Column(name = "reservation_id")
    private Integer reservationId;

    @Column(name = "user_id")
    private Integer userId;
}