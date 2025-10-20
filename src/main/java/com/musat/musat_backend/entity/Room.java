package com.musat.musat_backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "회의실")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder

public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "회의실 ID")
    private Integer id;

    @Column(name = "회의실 이름", length = 100, nullable = false)
    private String name;

    @Column(name = "회의실 종류", length = 20, nullable = false)
    private String type; // "스매시룸" 또는 "큐브"

    @Column(name = "수용 인원", nullable = false)
    private Integer capacity;


    public Room(String name, String type, Integer capacity) {
        this.name = name;
        this.type = type;
        this.capacity = capacity;
    }
}
