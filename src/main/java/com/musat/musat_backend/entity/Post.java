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
@EntityListeners(AuditingEntityListener.class)
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Integer id;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoomType roomtype; // CUBE or SMASH

    // [추가됨] 분실/발견/기타 카테고리
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PostCategory postCategory;

    @Column(length = 2251)
    private String photoUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder
    public Post(String title, String content, RoomType roomtype, PostCategory postCategory, String photoUrl, User user) {
        this.title = title;
        this.content = content;
        this.roomtype = roomtype;
        this.postCategory = postCategory; // [추가됨]
        this.photoUrl = photoUrl;
        this.user = user;
    }

    // [수정됨] update 메소드에 postCategory 추가
    public void update(String title, String content, RoomType roomtype, PostCategory postCategory, String photoUrl) {
        this.title = title;
        this.content = content;
        this.roomtype = roomtype;
        this.postCategory = postCategory; // [추가됨]
        this.photoUrl = photoUrl;
    }
}