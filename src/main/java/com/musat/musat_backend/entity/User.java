package com.musat.musat_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "사용자")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "사용자 id")
    private Integer id;

    @Column(name = "사용자 이름", length = 50, nullable = false)
    private String name;

    @Column(name = "비밀번호", length = 255, nullable = false)
    private String password;

    @Column(name = "사용자 이메일", length = 255, nullable = false, unique = true)
    private String email;

    @Column(name = "가입일시", nullable = false)
    private LocalDateTime joinedAt;

    @PrePersist
    public void prePersist() {
        this.joinedAt = LocalDateTime.now();
    }
}
