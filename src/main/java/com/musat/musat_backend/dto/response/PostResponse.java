package com.musat.musat_backend.dto.response;

import com.musat.musat_backend.entity.Post;
import com.musat.musat_backend.entity.PostCategory;
import com.musat.musat_backend.entity.RoomType;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostResponse {
    private Integer postId;
    private String username; // 작성자 이름
    private String title;
    private String content;
    private RoomType roomtype;
    private PostCategory postCategory;
    private String photoUrl;
    private LocalDateTime createdAt;

    public PostResponse(Post post) {
        this.postId = post.getId();
        this.username = post.getUser().getName();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.roomtype = post.getRoomtype();
        this.postCategory = post.getPostCategory();
        this.photoUrl = post.getPhotoUrl();
        this.createdAt = post.getCreatedAt();
    }
}