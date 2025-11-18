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

    @Column(nullable = false, unique = true, length = 20, name = "사용자 학번")
    private String studentId;

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

    @Builder
    public User(String username, String password, String email, String studentId) { // 생성자에도 추가
        this.name = name;
        this.password = password;
        this.email = email;
        this.studentId = studentId;
    }
}
