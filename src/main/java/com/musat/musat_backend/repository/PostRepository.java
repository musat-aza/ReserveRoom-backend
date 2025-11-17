package com.musat.musat_backend.repository;

import com.musat.musat_backend.entity.Post;
import com.musat.musat_backend.entity.PostCategory;
import com.musat.musat_backend.entity.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {

    /**
     * 동적 필터링 쿼리
     * - roomType이 null이면 전체 RoomType 조회
     * - postCategory가 null이면 전체 PostCategory 조회
     */
    @Query("SELECT p FROM Post p " +
            "WHERE (:roomType IS NULL OR p.roomtype = :roomType) " +
            "AND (:postCategory IS NULL OR p.postCategory = :postCategory) " +
            "ORDER BY p.createdAt DESC") // 최신순 정렬
    List<Post> findFilteredPosts(
            @Param("roomType") RoomType roomType,
            @Param("postCategory") PostCategory postCategory
    );
}