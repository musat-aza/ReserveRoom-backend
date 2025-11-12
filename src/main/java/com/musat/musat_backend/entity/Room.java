package com.musat.musat_backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "회의실")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "회의실 ID")
    private Integer id;

    @Column(name = "회의실 이름", length = 100, nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "회의실 종류", length = 20, nullable = false)
    private RoomType type; // Enum → 스매시룸, 큐브

    @Column(name = "수용 인원", nullable = false)
    private Integer capacity;
}
