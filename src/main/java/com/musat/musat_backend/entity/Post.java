package com.musat.musat_backend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class) // createdAt 자동화를 위해 추가
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Integer id; // ID 타입 통일 (Integer)

    @Column(nullable = false, length = 150)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @CreatedDate // 엔티티 생성 시 시간 자동 저장
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // ERD에 정의된 roomtype (RoomType Enum 재활용)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoomType roomtype;

    @Column(length = 2251) // ERD의 photolo -> photoUrl로 변경
    private String photoUrl;

    // 작성자 (User 엔티티와 N:1 관계)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PostCategory postCategory;

    @Builder
    public Post(String title, String content, RoomType roomtype, String photoUrl, User user) {
        this.title = title;
        this.content = content;
        this.roomtype = roomtype;
        this.photoUrl = photoUrl;
        this.user = user;
    }

    // 수정을 위한 update 메소드
    public void update(String title, String content, RoomType roomtype, String photoUrl) {
        this.title = title;
        this.content = content;
        this.roomtype = roomtype;
        this.photoUrl = photoUrl;
    }
}