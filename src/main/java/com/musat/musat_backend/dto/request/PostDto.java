package com.musat.musat_backend.dto.request;

import com.musat.musat_backend.entity.PostCategory;
import com.musat.musat_backend.entity.RoomType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostDto {
    private Integer userId; // 작성자 ID (예약처럼 DTO에서 받음)
    private String title;
    private String content;
    private RoomType roomtype;
    private PostCategory postCategory; // [추가됨] (LOST, FOUND, OTHER)
    private String photoUrl;
}