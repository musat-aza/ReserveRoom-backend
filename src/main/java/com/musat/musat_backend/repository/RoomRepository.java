package com.musat.musat_backend.repository;

import com.musat.musat_backend.entity.Room;
import com.musat.musat_backend.entity.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {
    List<Room> findByType(RoomType type);

    // [수정됨] 조건에 맞는 '방의 ID'만(List<Integer>) 조회합니다.
    @Query("SELECT DISTINCT r.id FROM Reservation res " +
            "JOIN res.room r " +
            "WHERE (:now BETWEEN res.startTime AND res.endTime) " +
            "AND r.type = :type")
    List<Integer> findActiveRoomIdsByType(@Param("now") LocalDateTime now, @Param("type") RoomType type);
}