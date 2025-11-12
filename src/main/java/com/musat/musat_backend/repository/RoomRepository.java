package com.musat.musat_backend.repository;

import com.musat.musat_backend.entity.Room;
import com.musat.musat_backend.entity.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {
    List<Room> findByType(RoomType type);
}