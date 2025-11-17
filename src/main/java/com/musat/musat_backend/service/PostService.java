package com.musat.musat_backend.service;

import com.musat.musat_backend.dto.request.PostDto;
import com.musat.musat_backend.dto.response.PostResponse;
import com.musat.musat_backend.entity.*;
import com.musat.musat_backend.repository.PostRepository;
import com.musat.musat_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    // 1. 게시글 생성
    public PostResponse createPost(PostDto request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Post post = Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .roomtype(request.getRoomtype())
                .postCategory(request.getPostCategory())
                .photoUrl(request.getPhotoUrl())
                .user(user)
                .build();

        postRepository.save(post);
        return new PostResponse(post);
    }

    // 2. 게시글 단건 조회
    @Transactional(readOnly = true)
    public PostResponse getPost(Integer postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        return new PostResponse(post);
    }

    // 3. 게시글 목록 조회 (필터링)
    @Transactional(readOnly = true)
    public List<PostResponse> getAllPosts(RoomType roomType, PostCategory postCategory) {
        // roomType이나 postCategory가 null로 들어오면 Repository 쿼리가 알아서 전체 조회
        return postRepository.findFilteredPosts(roomType, postCategory).stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());
    }

    // 4. 게시글 수정
    public PostResponse updatePost(Integer postId, PostDto request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        // (A) 작성자 본인 확인 로직 (선택 사항)
        // if (!post.getUser().getId().equals(requestDto.getUserId())) {
        //    throw new IllegalArgumentException("수정 권한이 없습니다.");
        // }

        post.update(
                request.getTitle(),
                request.getContent(),
                request.getRoomtype(),
                request.getPostCategory(),
                request.getPhotoUrl()
        );
        return new PostResponse(post);
    }

    // 5. 게시글 삭제
    public void deletePost(Integer postId) {
        if (!postRepository.existsById(postId)) {
            throw new IllegalArgumentException("게시글을 찾을 수 없습니다.");
        }
        // (B) 작성자 본인 확인 로직 (선택 사항)
        postRepository.deleteById(postId);
    }
}