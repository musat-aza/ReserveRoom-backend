package com.musat.musat_backend.controller;

import com.musat.musat_backend.dto.request.PostDto;
import com.musat.musat_backend.dto.response.PostResponse;
import com.musat.musat_backend.entity.PostCategory;
import com.musat.musat_backend.entity.RoomType;
import com.musat.musat_backend.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    // 1. 게시글 생성
    @PostMapping
    public ResponseEntity<PostResponse> createPost(@RequestBody PostDto request) {
        return ResponseEntity.ok(postService.createPost(request));
    }

    // 2. 게시글 단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPost(@PathVariable Integer id) {
        return ResponseEntity.ok(postService.getPost(id));
    }

    // 3. 게시글 목록 조회 (필터링)
    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPosts(
            @RequestParam(required = false) RoomType roomType,
            @RequestParam(required = false) PostCategory postCategory
    )
    {
        return ResponseEntity.ok(postService.getAllPosts(roomType, postCategory));
    }

    // 4. 게시글 수정
    @PutMapping("/{id}")
    public ResponseEntity<PostResponse> updatePost(
            @PathVariable Integer id,
            @RequestBody PostDto request
    ) {
        return ResponseEntity.ok(postService.updatePost(id, request));
    }

    // 5. 게시글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Integer id) {
        postService.deletePost(id);
        return ResponseEntity.ok().build();
    }
}